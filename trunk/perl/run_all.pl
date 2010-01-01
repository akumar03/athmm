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

my $EMPTY = "                               ";
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

my $BLOSUM_FILE = "/cluster/tufts/protein/se2/athmm/SortedBlosum62.csv";
my $AA_LETTERS = "ACDEFGHIKLMNPQRSTVWY";
my @AAF = (0.074,0.025,0.054,0.054,0.047,0.074,0.026,0.068,0.099,0.058,0.025,0.045,0.039,0.034,0.052, 0.057,0.051,0.073,0.013,0.034);
my $L = 0.347;

my @probs =load_blosum();


open(OUT,">ext_mut_all.txt");
print OUT " \t0\t";
for(my $mrate = 0;$mrate<=$MAX_RATE;$mrate +=10) {
    for(my $srate=0;$srate<=$MAX_SRATE;$srate +=5) {
      print OUT $mrate."_".$srate."\t";
   }
}
print OUT "\n";
  open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
  while(my $exp_line = <INPUT>){
   my @exp_words = split(',',$exp_line);
   my $exp_class = $exp_words[0];
   @sequences = ();
   print  OUT "$exp_class\t"; 
   my $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_class."_0_0.hmm ".$exp_class."_0_matt.ssi";
   print "$com\n";
   print `$com`;
    
  $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class."_0_0.hmm ".$exp_class."_test.fasta > ".$exp_class."_0_0.hit";
   print "$com\n";
   print `$com`;
   $com = "perl /cluster/tufts/protein/se2/athmm/perl/mep.pl ".$exp_class."_0_0.hit";
   print "$com\n";
   print `$com`;
   my $mep = read_mep();
   print OUT "$mep\t";
   $total_beta = 0; 
   my $ssi_file = $exp_words[0]."_0_matt.ssi";
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
    close (SSI);
   for(my $mrate = 0;$mrate<=$MAX_RATE;$mrate +=10) {
    for(my $srate=0;$srate<=$MAX_SRATE;$srate +=5) {
      @new_sequences = ();
#adding beta mutated sequences
    for(my $i=0;$i<=$#sequences;$i++) {
       push(@new_sequences,$sequences[$i]);
       if($mrate>0) {
        for(my $j =0;$j<$N;$j++) {
          my $new_sequence = get_beta_mutated_sequence($sequences[$i],$mrate,$j);
          push(@new_sequences,$new_sequence);
        }
       }
       if($srate>0) {

        for(my $j =0;$j<$N;$j++) {
 	  push(@new_sequences,get_mutated_sequence($sequences[$i],$j,$srate));
         }
      }
    }    
       
    $exp_class =  $exp_words[0]."_".$mrate."_".$srate;
    my $out_file = $exp_class.".ssi";
    print "Writing $out_file\n";
    write_out_file($out_file);

    $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_class.".hmm ".$exp_class.".ssi";
        print "$com\n";
    print `$com`;
    $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class.".hmm ".$exp_words[0]."_test.fasta > ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    $com = "perl /cluster/tufts/protein/se2/athmm/perl/mep.pl ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    usleep(100);
    my $mep = read_mep();
    print OUT $mep."\t"; 
    print "$exp_class MEP $mep\n"; 
    usleep(100);
    }
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
 my $new_label = $seq_label."_k".$count;
 my $new_sequence = $new_label.substr($EMPTY,0,21-length($new_label)).$seq_letters;
# print "label: ".$new_label." len:".length($new_label)."\n";
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
  open(OUTS,">$out_file");
  print OUTS "# STOCKHOLM 1.0\n";
  for(my $i=0;$i<= $#new_sequences;$i++) {
     print OUTS $new_sequences[$i];
  }
  print OUTS "//\n";
  close OUTS;
}

sub load_blosum {
  my @probs;
  open(BLOSUM,$BLOSUM_FILE) or die("Can't open blosum file");
  my $count = 0;
  while(my $line = <BLOSUM>) {
    my @words = split(",",$line);
    my $total = 0;
    for(my $i =0;$i<=$#words;$i++) {
       my $freq = int($words[$i]);
       $probs[$count][$i] = (10**($L*$freq))*$AAF[$count]*$AAF[$i];
#       print $probs[$count][$i]." ".$words[$i]."\n";
        $total += $probs[$count][$i];
    }
   
   for(my $i =0;$i<=$#words;$i++) {
          $probs[$count][$i] =$probs[$count][$i]/$total;
#          print "$count $i ". $probs[$count][$i]."\n";
   }
    $count++;
  }
  close(BLOSUM);
  return @probs;
}

sub get_random {
  my $char = shift;
  my $random = rand();
   $char = uc $char;
  my $aa_index = index($AA_LETTERS,$char);
  my $mutate_index = 0;
  for(my $i=0;$i<20 && $random >0;$i++) {
     $random -= $probs[$aa_index][$i];
#     print "$char $aa_index $random $probs[$aa_index][$i] $i \n";
     $mutate_index = $i;
   }
   return substr($AA_LETTERS,$mutate_index,1);
}

sub read_fasta_file {
   my $file = shift;
   my @seqs;
   my $label;
   my $sequence;
   print "Reading file $file\n";
   open(FILE,$file) or die("Can't open $file");
   my $count = 0;
   while(my $line = <FILE>) {
#     chop($line);
#     print $line;
     if($line =~ m/\>/) {
          if($count > 0) {
                $sequence =~ s/\W//g;
                $sequence =~ s/\r\n//g;
                $sequence =~ s/\n//g;
                $label =~ s/\r$//;
                push(@seqs,$label."\t".$sequence);
#               print "count $count\n  $label $sequence \n";
          }
          $label = $line;
          $sequence ="";
          $count++;
     } else {
           $sequence .= $line;
    }
  }  
  push(@seqs,$label."\t".$sequence);
  return @seqs;
}

sub get_mutated_sequence {
  my $s = shift;
  my $index = shift;
  my $mutations = shift;
  my @s_parts = split(/\s+/,$s);
  my $seq_label = $s_parts[0];
  my $sequence = $s_parts[1];
  my $n_mutations = int($mutations*length($sequence)/100+0.5);
  for(my $i = 0;$i<$n_mutations;$i++) {
    my $mutation_index = int(rand()*length($sequence));
    while(substr($sequence,$mutation_index,1) ne "-") {
      $mutation_index = int(rand()*length($sequence));
    }
    $sequence = substr($sequence,0,$mutation_index).get_random(substr($sequence,$mutation_index,1)).substr($sequence,$mutation_index+1);
  }
 my $new_label = $seq_label."_a".$index;
#  print "$seq_label $index size:".length($new_label)."\n";
 my $new_s = $new_label.substr($EMPTY,0,21-length($new_label)).$sequence;
  return $new_s."\n";
}

