//Introduces a program that reports information about a DNA, by first asking the file the
// user wishes to use, so that in the end it prints out the region name, nucleotide sequence, 
// count and individual nucleotide mass percent, as well as if each individual strain is a protein. 

import java.util.*;
import java.io.*;

public  class DNA{
   public static final int MINIMUM_CODONS = 5;
   public static final int PERCENTAGE = 30;
   public static final int UNIQUE_NUCLEOTIDES = 4;
   public static final int NUCLEOTIDES_PER_CODON = 3;
   
   public static void main(String[] args)throws FileNotFoundException{
      Scanner console = new Scanner(System.in);
      Scanner input = intro(console);
      
      System.out.print("Output file name? ");                             
      File fileOutputName = new File(console.nextLine()); 
      PrintStream outputFile = new PrintStream(fileOutputName);
      
      double[] massOfCodon = {135.128,111.103,151.128,125.107}; 
      double[] totMassPercent = new double[4];
      while(input.hasNextLine()){
         String regionName = input.nextLine();
         String nucleotidesStrain = input.nextLine().toUpperCase();
         int[] nucleotidesCount = nucleotides(nucleotidesStrain);
         double totalMass = totalMass(nucleotidesStrain, nucleotidesCount);
         for(int i = 0; i < UNIQUE_NUCLEOTIDES; i++){ 
               totMassPercent[i] = Math.round((nucleotidesCount[i] * massOfCodon[i]) / 
               totalMass * 1000.0) / 10.0;
         } 
         String[] finalizedCodons = codonList(nucleotidesStrain);
         String isItAProtein = protein(finalizedCodons, totMassPercent);
         
         results(outputFile, regionName, nucleotidesStrain, nucleotidesCount, totalMass,
                 totMassPercent, finalizedCodons, isItAProtein);
      }
   }
   
   //Prints intro as well as asks user for the file that will be used.
   //Parameters Used:
   // <console> in order to get the desired file user inputs.
   public static Scanner intro(Scanner console)throws FileNotFoundException{
      System.out.println("This program reports information about DNA");
      System.out.println("nucleotide sequences that may encode proteins.");
      System.out.print("Input file name? ");
      File fileInput = new File(console.nextLine());
      return new Scanner(fileInput);
   }
   
   //Counts out the number of unique nucleotides in each protein and returns the counter of each.
   //Parameters Used: 
   // <nucleotidesStrain> so it is able to read every strain of protein in the file.
   public static int[] nucleotides(String nucleotidesStrain)throws FileNotFoundException{
      int[] count = new int[UNIQUE_NUCLEOTIDES];
      
      for(int i = 0; i < nucleotidesStrain.length(); i++){
         char nucleotide = nucleotidesStrain.charAt(i);
         if(nucleotide == 'A'){             
            count[0]++;                            
         }else if(nucleotide == 'C'){
            count[1]++;
         }else if(nucleotide == 'G'){
            count[2]++;
         }else if(nucleotide == 'T'){
            count[3]++;
         } 
      }
      return count;
   }
   
   //Prints out the overall results of the DNA by giving the region name, the spcefic nucleotide
   // sequence, nucleotide count, total mass percent of each unique nucleotide, list of codons, and
   // if the strain is a protein.
   //Parameters used: 
   // <outputFile> = prints out all of the resutls in a new txt file named by user.
   // <regionName> = prints the region name.
   // <nucleotidesStrain> = Prints the nucleotide sequence.
   // <nucleotidesCount> = prints how many of each unique nucleotide there is
   // <totalMass> = prints out all of the combined mass of each nucleotide plus junk mass
   // <totalMassPercent> = prints the mass percent of each nucleotide
   // <codonList> = prints the codon list 
   // <protein> = prints whether it is a protein or not
   public static void results(PrintStream outputFile, String regionName, String nucleotidesStrain,
                              int[] nucleotidesCount, double totalMass, double[] totalMassPercent,
                              String[] codonList, String protein){
      outputFile.println("Region Name: " + regionName);
      outputFile.println("Nucleotides: " + nucleotidesStrain);
      outputFile.println("Nuc. Counts: " + Arrays.toString(nucleotidesCount));
      outputFile.println("Total Mass%: " + Arrays.toString(totalMassPercent) + " of " + 
                         Math.round(totalMass * 10.0) / 10.0);
      outputFile.println("Codons List: " + Arrays.toString(codonList));
      outputFile.println("Is Protein?: " + protein);
      outputFile.println();
   }
   
   //Calculates the total mass of each nucleotide plus the junk mass combined
   //Parameters used:
   // <nucleotidesStrain> = read and multiply the count of each nucleotide by specific given mass.
   // <nucleotidesCount> in order to calculate the mass of each unique nucleotide u sing the array
   public static double totalMass(String nucleotidesStrain, int[] nucleotidesCount)
         throws FileNotFoundException{ 
      double[] massOfCodon = {135.128,111.103,151.128,125.107}; 
      double totalMass = 0.0;
      double totalMassFinal = 0.0;
      
      for(int i = 0; i < UNIQUE_NUCLEOTIDES; i++){ 
         totalMass = (nucleotides(nucleotidesStrain)[i] * massOfCodon[i]);
         totalMassFinal += totalMass;
      }
      for(int i = 0; i < nucleotidesStrain.length(); i++){
         char ch = nucleotidesStrain.charAt(i);
         if(ch == '-'){
            totalMassFinal += 100.0;
         }
      }
      return totalMassFinal;
   }
   
   //Returns the codon list of the strain in the form of an array
   //Parameters Used:
   // <nucleotidesStrain> so method can read the strain and group nucleotides into codons
   public static String[] codonList(String nucleotidesStrain){
      String newNucleotidesStrain = nucleotidesStrain.replace("-","");
      String[] codonList = new String[newNucleotidesStrain.length()/NUCLEOTIDES_PER_CODON];
      int index = 0;
      for(int i = 0; i < newNucleotidesStrain.length()/NUCLEOTIDES_PER_CODON; i++){
         String codonChar = "";
         for(int j = i * NUCLEOTIDES_PER_CODON; j < i * NUCLEOTIDES_PER_CODON + 
             NUCLEOTIDES_PER_CODON; j++){ 
            codonChar += newNucleotidesStrain.charAt(j); //I don't think the char.toString is necessary here
         }
         codonList[i] = codonChar;
      }
      return codonList;
   }
   
   //Returns a string that says whether or not the strain is a protein based off of conditions
   // given.
   //Parameters Used: 
   // <codonList> = gives the array that contain the codon of the strain.
   // <totalMassPercent> = used to give condition based off of combined percent mass of 
   //                      nucleotides.
   public static String protein(String[] codonList, double[] totalMassPercent){
      String isItAProtein = "";
      if(codonList[0].equals("ATG") && (codonList.length) >= MINIMUM_CODONS && (totalMassPercent[1]
         + totalMassPercent[2]) >= PERCENTAGE && (codonList[codonList.length - 1].equals("TAA") || 
         codonList[codonList.length - 1].equals("TAG") || 
         codonList[codonList.length - 1].equals("TGA"))){
         
         isItAProtein = "YES";  
      }else {
         isItAProtein = "NO";
      }
      return isItAProtein;
   }
}