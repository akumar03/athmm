# Program: aln2ssi.pl
# Developed by : Anoop Kumar
# Date: 12/23/2009
# Description: This program converts aln file to ssi

use strict;
my $id = $ARGV[0];

my @seqs = read_aln_file($id.".aln");
write_ssi_file($id.".ssi",@seqs);

sub read_aln_file {
   my $file = shift;
   my @seqs;
   my $label;
   my $sequence;
   print "Reading file $file\n";
   open(FILE,$file) or die("Can't open $file");
   my $count = 0;
   while(my $line = <FILE>) {
     if($line =~ m/\>/) {
          if($count > 0) {
                $sequence =~ s/\n//g;
                $label =~ s/\n//g;
                push(@seqs,$label."\t".$sequence);
          }
          $label = $line;
          $sequence ="";
          $count++;
     } else {
           $sequence .= $line;
    }
  }  
  $sequence =~ s/\n//g;
  $label =~ s/\n//g;
  push(@seqs,$label."\t".$sequence);
  return @seqs;
}

sub write_ssi_file {
  my $file = shift;
  my @seqs = @_;
  my $empty_string = "                   "; 
  print "Writing file $file\n";
  open(OUT,">".$file) or die("Can't open $file");
  print OUT "# STOCKHOLM 1.0\n";
  for(my $i=0;$i<=$#seqs;$i++) {
    my @s_parts = split("\t",$seqs[$i]);
    my $sequence = $s_parts[1];
    my @label_parts = split(/\|/,$s_parts[0]);
    my $label = substr($label_parts[0],1,7)."_".$label_parts[1];
    my $label_length = length($label);
    print OUT $label.substr($empty_string,0,20-$label_length).$sequence."\n";
  }
  print OUT "//\n";
  close OUT;
}
 
