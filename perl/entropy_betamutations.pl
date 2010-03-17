# Program: entropy_betamutations.pl
# Developed by : Anoop Kumar
# Date: 03/16/2010
# Description: The program computes the entropy of beta mutations

use lib "/cluster/tufts/protein/se2/athmm/perl";
use strict;
use Beta;

my $PROTEIN_LETTERS = "ACDEFGHIKLMNPQRSTVWY-";

my @exposed_prob;
my @burried_prob;

my @betas;
my @sequences;
my @new_sequences;
my $total_beta=0;


my $exp_file = $ARGV[0];
print "reading exp file $exp_file\n";
if($ARGV[1]) {
  srand($ARGV[1]);
}



# reading the ssi files
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>){
my @exp_words = split(',',$exp_line);
my $ssi_file = $exp_words[0]."_0_matt.ssi";
@betas = ();
@sequences = ();
@new_sequences = ();
$total_beta = 0;
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
my $entropy = get_entropy();
#print "Total beta = $total_beta\n";
print $entropy."\n";
close(SSI);
}

# compute the entropy

sub get_entropy {
  my $mean_entropy = 0;
  my $total_entropy = 0;
  my $pos_entropy =0;
  
  for(my $i =0; $i<= $#betas; $i++) {
    my $beta = $betas[$i];
    my $start1 = $beta->getStrand1();
    my $start2 = $beta->getStrand2();
    my $length = $beta->getLength();
  
    for(my $j=0;$j<=$length;$j++) {
        my @counts = (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
	for(my $k=0;$k<=$#sequences;$k++) {
	   my $char = substr($sequences[$k],$start1+$k,1);
  	   my $char_index = index($PROTEIN_LETTERS,$char);
  	   $counts[$char_index]++;
#	   print "$j $k $char $char_index $counts[$char_index]\n";
	}
   	my $entropy = compute_entropy(@counts);
# 	print "$j Entropy $entropy\n";
       if($entropy > 0) {
	 $pos_entropy++;
	 $total_entropy += $entropy;
       }
    } 
    
  }
  $mean_entropy = $total_entropy/$pos_entropy;
  return $mean_entropy;
}

sub compute_entropy {
  my @counts = @_;
  my @probs = (0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
  my $total = 0;
  my $entropy = 0;
  for(my $i =0;$i<20;$i++) {
    $total += $counts[$i];
  }
  if($total>0) {
  for(my $i =0;$i<20;$i++) {
   $probs[$i] =  $counts[$i]/$total;
   if($counts[$i] > 0) {
	$entropy += -1*$probs[$i]*log($probs[$i])/log(2);
   }
  }
 }
  return $entropy;
}


# Adds beta mutations to sequence with %mutations per total length of betas;
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
