# Program: mep.pl
# Developed by : Anoop Kumar
# Date: 12/16/2009
# Description: functions for computing MEP( total errors at minimum error point)

use strict;

my $input_file = $ARGV[0];
my @hits;
my $threshold = 1001; # maximum score to start with
my $new_threshold =1000;
open (IN,$input_file);
my $count =0;
while(my $line =<IN>) {
 if($count > 3 && !($line =~ m/------/) && length($line) >1) {
     push (@hits,$line);
 } 
 $count++;
}
close IN;
my $mep =100;
my $old_tp_rate =0;
my $old_fp_rate =0;
my $auc = 0;
while($new_threshold < $threshold) {
 $threshold = $new_threshold;
  my $p =0;
  my $n = 0;
  my $tp = 0;
  my $fp = 0;
  my $tn = 0;
  my $fn = 0;
  my $tp_rate =0;
  my $fp_rate =0;
  for(my $i=0;$i<=$#hits;$i++){
#    print $hits[$i];
    my @words = split(/\s+/,$hits[$i]);
#    print "$words[2] $words[9] \n";
    if($words[9] eq "positive") {
	$p++;
      if($words[2] > $threshold) {
	$tp++;
      } else{
	$fn++;
      }
    } else {
        $n++;
       if($words[2] > $threshold) {
	$fp++;
       } else {
	$tn++;
       }
    }
    if($threshold == $new_threshold) {
    if($words[2] < $new_threshold) {
	$new_threshold = $words[2];
    } 

    if($i== $#hits) {
	$new_threshold = $words[2];
    }
    }
  }
  $tp_rate = $tp/$p;
  $fp_rate = $fp/$n;
  my $total_errors = $fp+$fn; 
  my $error_percent = $total_errors*100/($p+$n);
  if($error_percent < $mep) { 
    $mep = $error_percent;
  }
  $auc += ($tp_rate+$old_tp_rate)*($fp_rate - $old_fp_rate)/2;
  print "$threshold $new_threshold $p $n $mep $error_percent $tp $tn $fp_rate $tp_rate $auc\n";
  $old_tp_rate = $tp_rate;
  $old_fp_rate = $fp_rate;
}
open (MEP,">mep.txt");
print MEP $mep;
close MEP;
