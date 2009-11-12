# Program : do_hmmbuild.pl
# Developed by : Anoop Kumar
# Date : 09/03/08
# Description : A program that finds hmms that match a sequence

$prog = "hmmer/hmmer-3.0a2/src/hmmsearch -E 1000 --max ";
$exp_file = "Scop95.csv";
print "reading exp file $exp_file";
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
$count =0;
while(($line = <INPUT>) && ($count <1)){
 print "Reading line $line";
 @words = split(',',$line);
#running the default search
 $exp_class = $words[0];
$com = $prog.$exp_class."_0.hmm ".$words[0]."_test.fasta > ".$exp_class."_0.hit";
   print "Executing:  $com\n";
  print `$com`;
#mutated 
$com = $prog.$exp_class."_20.hmm ".$words[0]."_test.fasta > ".$exp_class."_20.hit";
   print "Executing:  $com\n";
  print `$com`;
 for($j=10;$j<=50;$j+=10) {
  $exp_class = $words[0]."_0_matt_".$j;
  $com = $prog.$exp_class.".hmm2 ".$words[0]."_test.fasta > ".$exp_class.".hit";
   print "Executing:  $com\n";
  print `$com`;
 }
 $count++;
}
