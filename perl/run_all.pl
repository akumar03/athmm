# Program: run_all.pl
# Developed by : Anoop Kumar
# Date: 12/30/2009
# Description: This program runs the beta mutation experiment with 10-100% mutations and 0-25% simple mutations

use lib "/cluster/tufts/protein/se2/athmm/perl";
use strict;
use Time::HiRes qw(usleep);
use Beta;

my $exp_file = $ARGV[0];
my $PATH = "/cluster/tufts/protein/se2/athmm/perl/";

my $N =  150;
my $M = 150;
my $MUTATION = 20;
my $MIN_RATE = 10;
my $MAX_RATE = 60;

my $MIN_SRATE =5;
my $MAX_SRATE =25;


my $PROTEIN_LETTERS = "ACDEFGHIKLMNPQRSTVWY";
my $exposed_file = "ExposedProbability.csv";
my $burried_file = "BurriedProbability.csv";
#read the probability files
my @exposed_prob = read_probabiliy_file($exposed_file);
my @burried_prob = read_probabiliy_file($burried_file);
my @betas;
my @sequences;
my @new_sequences;
my $total_beta;


print "reading exp file $exp_file N=$N\n";
open(OUT,">ext_mut_all.txt");
print OUT " \t0\t";
for(my $mrate = $MIN_RATE;$mrate<=$MAX_RATE;$mrate +=10) {
  print OUT "$mrate\t";
#  my $com = "perl /cluster/tufts/protein/se2/athmm/perl/generate_betamutations.pl $exp_file 0 $mrate $N";
#   print "$com\n";
#   print `$com`;
}
print OUT "\n";
  open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
  if(my $exp_line = <INPUT>){
   my @exp_words = split(',',$exp_line);
   my $exp_class = $exp_words[0];
   print  OUT "$exp_class\t"; 
   my $com = "perl ".$PATH."aln2ssi.pl $exp_class_file";
   print "$com\n";
   print `$com`;
    
   my $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class."_0_0.hmm ".$exp_class."_test.fasta > ".$exp_class."_0.hit";
   print "$com\n";
   print `$com`;
   $com = "perl /cluster/tufts/protein/se2/athmm/perl/mep.pl ".$exp_class."_0_0.hit";
   print "$com\n";
   print `$com`;
   my $mep = read_mep();
   print OUT "$mep\t";
   
   for(my $mrate = $MIN_RATE;$mrate<=$MAX_RATE;$mrate +=10) {
    $exp_class =  $exp_words[0]."_0_matt_".$mrate;
    my $ssi_file = $exp_words[0]."_0_matt_".$mrate.".ssi";

    
    $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_class.".hmm $ssi_file";
    print "$com\n";
    print `$com`;
    $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class.".hmm ".$exp_words[0]."_test.fasta > ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    $com = "perl /cluster/tufts/protein/se2/athmm/perl/mep.pl ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    my $mep = read_mep();
    print OUT "$mep\t"; 
    usleep(100);
    print "$exp_class MEP $mep\n"; 
   }
    print OUT "\n"; 
  }

close OUT;

sub read_mep {
   open(MEP,"mep.txt");
   my $line;
   if($line = <MEP>) {
   }
   close MEP;
   return $line;
}


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
#dds beta mutations to sequence with %mutations per total length of betas;
sub get_beta_mutated_sequence{
 my $sequence = shift;
 my $percent = shift;
 my $count = shift;
 
 my @seq_words= split(/\s+/,$sequence);
 my $seq_letters = @seq_words[1];
 my $seq_label = @seq_words[0];

 my $mutations = int(($percent*$total_beta/100)+0.5);
 for(my $j = 0;$j< $mutations;$j++) {
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
 my $aa = substr($seq_letters,$mutate_position,1);
 my $aa_mutated = get_mutated_aa($aa,$is_burried);
 my $seq_mutated = substr($seq_letters,0,$mutated_position).$aa_mutated.substr($seq_letters,$mutated_position+1);
 $seq_letters = $seq_mutated;
# print "$position $mutate_position $mutated_position $is_burried\n";
 }
 my $new_sequence = $seq_label."_m".$count."         ".$seq_letters;

# print "$position $mutate_position $mutated_position $is_burried\n";
# print "$position $mutate_position $mutated_position $is_burried\n";
# print $sequence;
# print "$new_sequence\n";
 return $new_sequence."\n";
 
}

sub get_mutated_aa {
 my $aa = shift;
 my $is_burried = shift;
 my @table = @exposed_prob;
 if($is_burried) {
   @table = @burried_prob;
 }
 my $random = rand();
 my $aa_index = index($PROTEIN_LETTERS,$aa);
 my $mutate_index =0;
 for(my $i=0;$i<20 && $random >0;$i++) {
   $random -= $table[$aa_index][$i];
   $mutate_index = $i;
 }
# return "X";
 return substr($PROTEIN_LETTERS,$mutate_index,1);


}

# write output sequences with mutated sequences

sub write_out_file {
  my $out_file = shift;
  open(OUT,">$out_file");
  print OUT "# STOCKHOLM 1.0\n";
  for(my $i=0;$i<= $#new_sequences;$i++) {
     print OUT $new_sequences[$i];
  }
  print OUT "//\n";
  close OUT;
}

