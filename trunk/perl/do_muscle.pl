# Program : do_muscle.pl
# Developed by : Anoop Kumar
# Date : 09/03/08
# Description : A program that performs muscle alignment

$prog_muscle = "/r/bcb/anoop/soft/muscle3.7/muscle ";

$exp_file = "astralStats.txt";
print "reading exp file $exp_file";

open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while($line = <INPUT>){
 print "Reading line $line";
 @words = split('\t',$line);
 for($j=0;$j<=0;$j+=5) {
  $exp_class = $words[0]."_".$j;
  $com1 = $prog_muscle." -out ".$exp_class.".aln -in ".$exp_class.".fasta";
   print "Executing:  $com1\n";
  print `$com1`;
 }
}
