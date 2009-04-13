# Program : do_hmmbuild.pl
# Developed by : Anoop Kumar
# Date : 09/03/08
# Description : A program that finds hmms that match a sequence

$prog = "/r/bcb/anoop/soft/hmmer-2.3.2/bin/hmmpfam -E 100  ";
$exp_file = "Scop95.csv";
print "reading exp file $exp_file";
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split(',',$line);
 for($j=0;$j<=25;$j+=5) {
  $exp_class = $words[0]."_".$j;
  $com1 = $prog.$exp_class.".hmm ".$words[0]."_test.fasta > ".$exp_class.".hit";
   print "Executing:  $com1\n";
  print `$com1`;
 }
}
