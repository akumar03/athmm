# Program: format_bubble.pl
# Developed by : Anoop Kumar
# Date: 1/4/2010
# Description: The program takes out from roc experiment and formats it for bubble plot

use strict;
my $exp_file = $ARGV[0];

my $MAX_RATE = 60;
my $MAX_SRATE =25;


open(INPUT,$exp_file) or die("Can't open the input file: $exp_file");
my $exp_line;
$exp_line = <INPUT>;
$exp_line = <INPUT>;

print "EXP $exp_line\n";
close INPUT;
open(OUT,">auc_bubble.txt");
my @exp_words = split(/\t/,$exp_line);

my $count =1;
for(my $mrate = 0;$mrate<=$MAX_RATE;$mrate +=10) {
    for(my $srate=0;$srate<=$MAX_SRATE;$srate +=5) {
    print OUT "$mrate\t$srate\t".$exp_words[$count]."\n";
    $count++;
}
}
close OUT;


