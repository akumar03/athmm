# Program: run_program.pl
# Developed by : Anoop Kumar
# Date: 11/10/2009
# Description: A generic wrapper to run pgrograms

use strict;


my $exp_file = $ARGV[0];
print "reading exp file $exp_file\n";

my $program = $ARGV[1];
my $ext1 = $ARGV[2];
my $ext2 = $ARGV[3];
print "Running  $program with $ext1 and $ext2\n";

# reading the ssi files
open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
while(my $exp_line = <INPUT>){

my @exp_words = split(',',$exp_line);
my $com = "perl ".$program." ".$exp_words[0]."_0.".$ext1."  > ".$exp_words[0]."_0.".$ext2;
print "$com\n";
print `$com`;
$com = "perl ".$program." ".$exp_words[0]."_20.".$ext1."  > ".$exp_words[0]."_20.".$ext2;
print "$com\n";
print `$com`;
for(my $percent=0;$percent<=50;$percent += 10) {
  $com = "perl ".$program." ".$exp_words[0]."_0_matt_".$percent.".".$ext1." > ".$exp_words[0]."_0_matt_".$percent.".".$ext2;
  print "$com\n";
  print `$com`; 
}
}
close INPUT;
