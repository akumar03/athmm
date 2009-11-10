# Program: generate_betamutations.pl
# Developed by : Anoop Kumar
# Date: 11/06/2009
# Description: The program appends sequences with beta mutations


use strict;

my $PROTEIN_LETTERS = "ACDEFGHIKLMNPQRSTVWY";
my $exposed_file = "ExposedProbability.csv";
my $burried_file = "BurriedProbability.csv";


my $ssi_file = "b.1.1.3_0_matt.ssi";

my $out_file = "b.1.1.3_0_matt_mut.ssi";

open(OUT,">$out_file");

# reading the ssi files
open(SSI,$ssi_file);
while(my $line = <SSI>) {

}
close(SSI);

#read the 



my @exposed_prob = read_probabiliy_file($exposed_file);
my @burried_prob = read_probabiliy_file($burried_file);




# This method reads the probability file
sub read_probabiliy_file {
  my $file = shift;
  my @p;
  my  $i = 0;
  open (INPUT,$file);
  while( my $line = <INPUT>) {
   my @words = split(/,/,$line);
   for(my $j =0;$j <20 && $j<=$#words ;$j++) { 
     $p[$i][$j] = $words[$j];
   }
   $i++;
  }
  close(INPUT);
  return @p;
}


