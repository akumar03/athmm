# Program: make_plots.pl
# Developed by : Anoop Kumar
# Date: 11/10/2009
# Description: This program makes plots  using gnuplot

use strict;


my $exp_file = $ARGV[0];
print "reading exp file $exp_file\n";


# reading the ssi files
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>){
my @exp_words = split(',',$exp_line);
my $plot = "set terminal png \n";
$plot .= "set output 'plots/".$exp_words[0].".png' \n";
$plot .= "set yrange [0:1.1]\n";
$plot .= "set xrange [0:1.1]\n";
$plot .= "set xlabel 'False Positive Rate' \n";
$plot .= "set ylabel 'True Positive Rate'\n";

$plot .= "plot ";
$plot .= " \"".$exp_words[0]."_0.roc\" using 7:8 with line lw 2 title 'HMM0', ";
$plot .= " \"".$exp_words[0]."_20.roc\" using 7:8 with line lw 2 title 'HMM1', ";
for(my $percent=10;$percent<=50;$percent += 10) {
  $plot .= " \"".$exp_words[0]."_0_matt_".$percent.".roc\" using 7:8 with line lw 2 title 'HMM2_$percent'";
  if($percent != 50) {
   $plot .= ", ";
  }
}
$plot .= "\n";
$plot .= "set output\n";
$plot .= "exit\n";
open(PLT,">".$exp_words[0].".plt");
print PLT $plot;
close PLT;
my $com = "gnuplot ".$exp_words[0].".plt";
print "$com\n";
print `$com`;
}
close INPUT;
