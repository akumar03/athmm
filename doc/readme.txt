1. Folders
*  C:\anoop\courses\bio\research\function\local contains all the files related to experiments
* scop95 and scop40 folders contains astal protein sequences with less than 95% and 40% similarity respectively
* C:\anoop\courses\bio\soft\code\athmm contains all the java files and the source code

2. File types
* astral.fasta is the raw astral file with protein sequences in fasta foramt
* asttralStats.txt contains stats about the astral file. It is comma separated with
  family, nuber of sequences in family, number of sequences in the superfamily but not in the family (+ve test) and
  number of sequences that belong to other fold (-ve test sequences)
* [family_id]_[mutation].fasta is the fasta file for family with number of mutations
* .aln are alignment files
* [family_id]_test.fasta is the test file for the family with family_id
* [family_id]_test_all.fasta test sequences with all -ver sequences
* [family_id]_0.aln alignment with 0 mutations
* [family_id]_20.aln alignment with 20% mutations in random positions
* [family_id]_20NE.aln alignment with 20% mutations with preference to adding mutations in conserved regions
* [family_id]_20NEX.aln opposite of the 20NE
