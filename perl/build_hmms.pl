# Program: build_hmms.pl
# Developed by : Anoop Kumar
# Date: 11/10/2009
# Description: The program builds hmms

use strict;


my $exp_file = $ARGV[0];
print "reading exp file $exp_file";

# reading the ssi files
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>){

my @exp_words = split(',',$exp_line);
my $ssi_file = $exp_words[0]."_0_matt.ssi";
my $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_words[0]."_0_matt.hmm $ssi_file";
print "$com\n";
print `$com`;
for(my $percent=10;$percent<=50;$percent += 10) {
  $ssi_file = $exp_words[0]."_0_matt_".$percent.".ssi";
  $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_words[0]."_0_matt_".$percent.".hmm $ssi_file";
  print "$com\n";
  print `$com`; 
}
}
close INPUT;
