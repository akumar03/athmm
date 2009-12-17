# Program: report.pl
# Developed by : Anoop Kumar
# Date: 11/16/2009
# Description: This program creates a report for the test set

use strict;


my $program = "/cluster/tufts/protein/se2/athmm/perl/roc.pl ";
my $exp_file = $ARGV[0];
print "reading exp file $exp_file\n";


# reading the ssi files
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
my $s;
while(my $exp_line = <INPUT>){

my @exp_words = split(',',$exp_line);
$s .= $exp_words[0].",";
my $com = "perl ".$program.$exp_words[0]."_0.hit"  ;
print "$com\n";
print `$com`;
my $imp_flag = 0;
my $auc0 = read_roc();
$s .= $auc0.",";
$com = "perl ".$program.$exp_words[0]."_20.hit"  ;
print "$com\n";
print `$com`;
$s .= read_roc().",";

for(my $percent=10;$percent<=50;$percent += 10) {
   $com = "perl ".$program.$exp_words[0]."_0_matt_".$percent.".hit"  ;
   print "$com\n";
   print `$com`;
   my $auc = read_roc();
   $s .= $auc;
   $s .= ",";
   if($auc>($auc0*1.05)) {
     $imp_flag =1;
   }
}
$s .= $imp_flag;
$s .="\n";
}
close INPUT;
open (RESULTS,">results.csv");
print RESULTS $s;
close RESULTS;


sub read_roc {
   open(ROC,"roc.txt");
   my $line;
   if($line = <ROC>) {
   }
   close ROC;
   return $line;
}
