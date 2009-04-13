# Program : do_hmmbuild.pl
# Developed by : Anoop Kumar
# Date : 01/06/09
# Description : A program that builds hmm from given clustal alignment. Modified to use entropy measure

$prog = "/r/bcb/anoop/soft/hmmer-2.3.2/bin/hmmbuild --wme --fast --gapmax 0.1  ";
$calibrate = "/r/bcb/anoop/soft/hmmer-2.3.2/bin/hmmcalibrate --seed 0 ";
$exp_file = "Scop95.csv";
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split(',',$line);
 for($j=0;$j<=25;$j+=5) {
  $exp_class = $words[0]."_".$j;
  $com1 = $prog.$exp_class.".hmm ".$exp_class.".aln";
  print "Executing:  $com1\n";
  print `$com1`;
  $com2 = $calibrate.$exp_class.".hmm";
  print  "Executing:  $com2\n";
  print `$com2`;
 }
}
