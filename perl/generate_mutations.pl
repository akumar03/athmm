# Program: generate_mutations.pl
# Developed by : Anoop Kumar
# Date: 12/22/2009
# Description: The program generates mutations based on blosum62

use strict;
my $id = $ARGV[0];
my $N = 10;
if($ARGV[1]) {
   $N= $ARGV[1];
} 
if($ARGV[2]) {
   srand($ARGV[2]);
} else {
  srand(0);
}

my $BLOSUM_FILE = "/cluster/tufts/protein/se2/athmm/SortedBlosum62.csv";
my $AA_LETTERS = "ACDEFGHIKLMNPQRSTVWY";
my @AAF = (0.074,0.025,0.054,0.054,0.047,0.074,0.026,0.068,0.099,0.058,0.025,0.045,0.039,0.034,0.052, 0.057,0.051,0.073,0.013,0.034);
my $L = 0.347;
my $MUTATIONS = 20;

my @probs =load_blosum();
my @seqs = read_fasta_file($id."_0.fasta");
my @new_seqs;
for(my $i =0; $i<=$#seqs;$i++) {
 push (@new_seqs,$seqs[$i]);
 for(my $j=1;$j<=$N;$j++) {
    push (@new_seqs,get_mutated_sequence($seqs[$i],$j,$MUTATIONS));
  }
}

write_fasta_file($id."_".$MUTATIONS."_a".$N.".fasta",@new_seqs);



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
# 	        print "count $count\n  $label $sequence \n";
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

sub write_fasta_file {
  my $file = shift;
  my @seqs = @_;
  open(OUT,">".$file) or die("Can't open $file");
  for(my $i=0;$i<=$#seqs;$i++) {
    my @s_parts = split("\t",$seqs[$i]);
    my $sequence = $s_parts[1];
    print OUT $s_parts[0];
    $sequence = uc $sequence;
    $sequence =~ s/\n//g;
    for(my $j=0;$j<=length($sequence)/50;$j++) {
	my $start = $j*50;
        my $stop = $start+50>length($sequence)?length($sequence):$start+50;
	print OUT substr($sequence,$start,$stop-$start);
        print OUT "\n";
    }
  }
  close OUT;
}

sub get_mutated_sequence {
  my $s = shift;
  my $new_s;
  my $index = shift;
  my $mutations = shift;
  my @s_parts = split("\t",$s);
  my $label = $s_parts[0];
  my $sequence = $s_parts[1];
  $label =~ s/mut0/mut$index/;
  my $n_mutations = int($mutations*length($sequence)/100+0.5);
  for(my $i = 0;$i<$n_mutations;$i++) {
    my $mutation_index = int(rand()*length($sequence)+0.5);
    $sequence = substr($sequence,0,$mutation_index).get_random(substr($sequence,$mutation_index,1)).substr($sequence,$mutation_index+1);
  }
  my $new_s = $label."\t".$sequence;
  return $new_s;
}
