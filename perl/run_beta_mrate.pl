# Program: run_beta_mrate.pl
# Developed by : Anoop Kumar
# Date: 12/28/2009
# Description: This program runs the beta mutation experiment with 10-100% mutations


use strict;
use Time::HiRes qw(usleep);

my $exp_file = $ARGV[0];
my $N =  150;
my $MUTATION = 20;
my $MIN_RATE = 10;
my $MAX_RATE = 100;
print "reading exp file $exp_file N=$N\n";


open(OUT,">ext_mut".$MIN_RATE."_".$MAX_RATE."_k".$N.".txt");
print OUT " \t0\t";
for(my $mrate = $MIN_RATE;$mrate<=$MAX_RATE;$mrate +=10) {
  print OUT "$mrate\t";
#  my $com = "perl /cluster/tufts/protein/se2/athmm/perl/generate_betamutations.pl $exp_file 0 $mrate $N";
#   print "$com\n";
#   print `$com`;
}
print OUT "\n";
  open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
  while(my $exp_line = <INPUT>){
   my @exp_words = split(',',$exp_line);
   my $exp_class = $exp_words[0];
   print  OUT "$exp_class\t"; 
   my $com = "/cluster/tufts/protein/se2/hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ".$exp_class."_0.hmm ".$exp_class."_test.fasta > ".$exp_class."_0.hit";
   print "$com\n";
   print `$com`;
   $com = "perl /cluster/tufts/protein/se2/athmm/perl/mep.pl ".$exp_class."_0.hit";
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
