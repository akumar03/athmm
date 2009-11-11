# Program: generate_betamutations.pl
# Developed by : Anoop Kumar
# Date: 11/06/2009
# Description: The program appends sequences with beta mutations

use lib "/cluster/tufts/protein/se2/athmm/perl";
use strict;
use Beta;

my $PROTEIN_LETTERS = "ACDEFGHIKLMNPQRSTVWY";
my $N = 10; # mumber of mutated sequences to be added
my $exposed_file = "ExposedProbability.csv";
my $burried_file = "BurriedProbability.csv";


my $ssi_file = "b.1.1.3_0_matt.ssi";

my $out_file = "b.1.1.3_0_matt_mut.ssi";
my @betas;
my @sequences;
my @new_sequences;
my $total_beta=0;

srand(1);

open(OUT,">$out_file");

# reading the ssi files
open(SSI,$ssi_file);
while(my $line = <SSI>) {
 if($line =~ m/^#=/) {
  my @words = split(/\s+/,$line);
  my $beta = new Beta($words[1],$words[2],$words[3],$words[5],$words[6]);
  $total_beta += $words[3];  # assuming no overlap
  push(@betas,$beta);
 }
 if($line =~ m/^d/) {
   push (@sequences,$line);
 } 
}

$total_beta *=2;


print "Total beta = $total_beta\n";
#adding mutated sequences
for(my $i=0;$i<=$#sequences;$i++) {
#  print $sequences[$i];
  add_beta_mutations($sequences[$i],10);
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

# Adds beta mutations to sequence with %mutations per total length of betas;
sub add_beta_mutations  {
 my $sequence = shift;
 my $percent = shift; 
 my $position = int(rand($total_beta));
 my $position_pointer  = $position;
 my $mutate_position = -1;
 my $mutated_position = -1;
 my $is_burried = 0;
 my $selected_beta;
 my $mutate_strand; 
 for(my $i =0; $i<= $#betas && $mutate_position < 0;$i++) {
   my $beta = $betas[$i];
   my $start1 = $beta->getStrand1();
   my $start2 = $beta->getStrand2();
   my $length = $beta->getLength();
#   print "$start1 $start2 $length $position_pointer  \n";
   if($position_pointer <= $length) {
       $mutate_position = $start1+$position_pointer;
       $selected_beta = $beta;
       $mutate_strand = 1;
       if(substr($beta->getConformation(),$position_pointer,1) eq 'i') {
 	 $is_burried = 1;
       }
   } elsif($position_pointer <= 2*$length) {
      $position_pointer -= $length;
       $mutate_position = $start2+$position_pointer;
       $selected_beta = $beta;
       $mutate_strand = 2;
       if($beta->getParellel() == -1) {
         if(substr(reverse($beta->getConformation()),$position_pointer,1) eq 'i') {
 	   $is_burried = 1;
         }

       } else {
         if(substr($beta->getConformation(),$position_pointer,1) eq 'i') {
 	   $is_burried = 1;
         }
      } 
   } else {
      $position_pointer -= 2*$length;
   }
 }
  
# computing the mutated position
 if($selected_beta->getParellel() == -1) {
  if($mutate_strand == 1) {
     my $pointer = $mutate_position-$selected_beta->getStrand1();
     $mutated_position = $selected_beta->getStrand2()+$selected_beta->getLength()-$pointer-1;
  } else {
     my $pointer = $mutate_position-$selected_beta->getStrand2();
     $mutated_position = $selected_beta->getStrand1()+$selected_beta->getLength()-$pointer-1;
  }
 } else {
  if($mutate_strand == 1) {
     my $pointer = $mutate_position-$selected_beta->getStrand1();
     $mutated_position = $selected_beta->getStrand2()+$pointer;
  } else {
     my $pointer = $mutate_position-$selected_beta->getStrand2();
     $mutated_position = $selected_beta->getStrand1()+$pointer;
  }
 }

 print "$position $mutate_position $mutated_position $is_burried\n";
 
 
}

