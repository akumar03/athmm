# Program: auc_percent.pl
# Developed by : Anoop Kumar
# Date: 1/4/2010
# Description: The program takes out from roc experiment and formats it for bubble plot

use strict;
my $exp_file = $ARGV[0];



open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
my $exp_line;
$exp_line = <INPUT>;
print "EXP $exp_line\n";
open(OUT,">auc_percent.txt");
print OUT $exp_line;
while($exp_line = <INPUT>) {
 my @words = split(/\t/,$exp_line);
 print OUT $words[0]."\t";
 for(my $i = 1;$i<=$#words;$i++) {
  my $percent = ($words[$i]-$words[1])/$words[1];
  print OUT $percent."\t";
 }
 print OUT "\n";
}
close INPUT;
close OUT;


