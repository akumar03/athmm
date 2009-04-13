# Program : do_hmmsearch.pl
# Developed by : Anoop Kumar
# Date : 08/19/08
# Description : A program that emints hmm sequences

$func_folder ="/r/bcb/anoop/function/";
$exp_folder =$func_folder."gpcr_001_001_gpcr_001_002/";

$exp_file = $exo_folder."gpcrStatsFiltered.csv";

print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file");
while($line = <INPUT>){
  print "Reading line $line";
  @words = split(',',$line);
  $exp_family = $words[0];
  $com1 = "/r/bcb/anoop/soft/hmmer-2.3.2/bin/hmmemit -n 1000 -o $exp_folder/gpcr_".$exp_family."_0_hmm.fasta  $exp_folder/gpcr_".$exp_family."_0.hmm";
   print "Executing:  $com1\n";
   print `$com1`;
  $com2 = "/r/bcb/anoop/soft/hmmer-2.3.2/bin/hmmemit -n 1000 -o $exp_folder/gpcr_".$exp_family."_1_hmm.fasta $exp_folder/gpcr_".$exp_family."_1.hmm";
   print "Executing:  $com2\n";
   print `$com2`;
}
close(INPUT);
