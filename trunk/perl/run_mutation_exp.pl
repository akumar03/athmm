# Program: run_mutation_exp.pl
# Developed by : Anoop Kumar
# Date: 12/23/2009
# Description: This program run creates an HMM with blosum mutations 40 times and assess the stability


use strict;
use Time::HiRes qw(usleep);

my $exp_file = $ARGV[0];
my $N = $ARGV[1];
my $MUTATION = 20;
my $PATH = "/cluster/tufts/protein/se2/athmm/perl/";
print "reading exp file $exp_file N=$N\n";


open(OUT,">ext_mut".$MUTATION."_k".$N.".txt");

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");

print OUT " \t";
while(my $exp_line = <INPUT>){
 my @exp_words = split(',',$exp_line);
 print OUT $exp_words[0]."\t";
}
close INPUT;
print OUT "\n";

for(my $i=0;$i<1;$i++) {
  open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
  if(my $exp_line = <INPUT>){
    my @exp_words = split(',',$exp_line);
    my $exp_class =  $exp_words[0];
    my $exp_class_file =  $exp_class."_".$MUTATION."_a".$N;
    my $com = $PATH."generate_mutations.pl $exp_class $N $i";
    print "$com\n";
    print `$com`;
    $com = "/cluster/tufts/protein/muscle/mus4 -i ".$exp_class_file.".fasta -o ".$exp_class_file.".aln";
    print "$com\n";
    print `$com`;
    $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmbuild ".$exp_class.".hmm $ssi_file";
    print "$com\n";
    print `$com`;
    $com = "hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class.".hmm ".$exp_words[0]."_test.fasta > ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    $com = "perl athmm/perl/mep.pl ".$exp_class.".hit";
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
