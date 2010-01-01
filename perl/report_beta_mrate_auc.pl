# Program: run_beta_mrate.pl
# Developed by : Anoop Kumar
# Date: 12/29/2009
# Description: This program reports AUC for the the beta mutation experiment with 10-100% mutations


use strict;
use Time::HiRes qw(usleep);

my $exp_file = $ARGV[0];
my $N =  150;
my $MUTATION = 20;
my $MIN_RATE = 10;
my $MAX_RATE = 100;
print "reading exp file $exp_file N=$N\n";


open(OUT,">auc_mut".$MIN_RATE."_".$MAX_RATE."_k".$N.".txt");
print OUT " \t0\t";
for(my $mrate = $MIN_RATE;$mrate<=$MAX_RATE;$mrate +=10) {
  print OUT "$mrate\t";
}
print OUT "\n";
  open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
  while(my $exp_line = <INPUT>){
   my @exp_words = split(',',$exp_line);
   my $exp_class = $exp_words[0];
   print  OUT "$exp_class\t"; 
   my $com = "perl /cluster/tufts/protein/se2/athmm/perl/roc.pl ".$exp_class."_0.hit";
   print "$com\n";
   print `$com`;
   my $auc0 = read_roc();
   my $pos_flag = 0;
   my $neg_flag = 0;
   print OUT "$auc0\t";
   for(my $mrate = $MIN_RATE;$mrate<=$MAX_RATE;$mrate +=10) {
    $exp_class =  $exp_words[0]."_0_matt_".$mrate;
    my $ssi_file = $exp_words[0]."_0_matt_".$mrate.".ssi";
    $com = "perl /cluster/tufts/protein/se2/athmm/perl/roc.pl ".$exp_class.".hit";
    print "$com\n";
    print `$com`;
    my $auc = read_roc();
    print OUT "$auc\t"; 
    if($auc>($auc0*1.05)) {
     $pos_flag =1;
   }
    if($auc<($auc0*.95)) {
     $neg_flag =1;
   }
    usleep(100);
    print "$exp_class AUC $auc\n"; 
   }
   print OUT "$pos_flag\t$neg_flag";
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

sub read_roc {
   open(ROC,"roc.txt");
   my $line;
   if($line = <ROC>) {
   }
   close ROC;
   return $line;
}


