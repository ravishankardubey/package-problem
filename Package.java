package whatfix;

/*importing packages : Starts Here*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
/*importing packages : Ends Here*/

/* class "Package" where the logic is coded : Starts Here */
class Package{

    
    /* variables declration for global scope and access : Starts Here*/
    private static String fileName=" ";
    private static double packageWeight;
    private static int[] index = new int[15];
    private static double[] itemWeight = new double[15];
    private static double[] itemCost = new double[15];
    private static int noOfElements;
    /* variables declration for global scope and access : Ends Here*/

    Package(){
      /* private constructor */  
    } 
    
    /* Main Method definition : Starts Here*/
    public static void main(String[] args){


        try{
            BufferedReader input = new BufferedReader (new InputStreamReader (System.in)); /* InputStream creation to take File Path from console */
            System.out.print("Enter File Path : ");
            fileName = input.readLine();
            if(" ".equals(fileName))
            {
                System.out.println("FIle Path Not Entered");
            }
            else{
                FileReader fr = new FileReader(fileName);          /* object creation of BufferedReader and FileReader to take input from file */
                BufferedReader br = new BufferedReader(fr);

                String line = br.readLine(); 
                if (line == null) {
                    System.out.println("file empty");       /* reading line from file, and print on console if empty */
                }
                else{

                    //Package newObject = new Package();      /* Object on Package class */
                    while ((line = br.readLine()) != null) { /* read lines from File untill it is empty */
                        Package.extractFromPattern(line); /* Method calling, to extract data from the input file*/
                        Package.maxCostPackage();         /* Method calling, to provide maximum cost of the packge */
                    }
                }
                br.close();
                fr.close();
            }

        }
        catch(IOException e){       /* Catches the IOException */
            System.out.println(e.toString());
        }

    }
    /* Main Method definition : Ends Here*/

    /* extractFromPatter definition,  to extract package and item details from file : Starts Here */
    public static void extractFromPattern(String str){

        int i = 0;
        int k = 0;
        noOfElements = 0;
        String weight = str.split(":")[0];      /* Splitting and storing package weight as string */
        packageWeight = Double.parseDouble(weight);        /* Conversion from String to Double */

        String itemString = str.split(":")[1];        /* Splitting and storing all item values in "str" */


        int count = itemString.length(); /* String Length */
        int start;             /* Start index to find substring */
        int end;                /* End index to find substring */
        while(i<=count-1)
        {
            start = itemString.indexOf('(',i);     /* Substring of first element i.e., index */
            end = itemString.indexOf(',',start);
            if(end - start == 2)            /* if the values is of single digit */
                index[k] = Character.getNumericValue(itemString.charAt(start+1));
            else
                index[k] = Integer.parseInt(itemString.substring(start+1,end));

            start = end;
            end = itemString.indexOf(',',start+1);     /* Substring of second element i.e., weight */
            if(end - start == 2)            /* if the values is of single digit */
                itemWeight[k] = Character.getNumericValue(itemString.charAt(start+1));
            else
                itemWeight[k] = Double.parseDouble(itemString.substring(start+1,end));

            start = itemString.indexOf('$',end);       /* Substring of third element i.e., cost */
            end = itemString.indexOf(')',start+1);
            if(end - start == 2)            /* if the values is of single digit */
                itemCost[k] = Character.getNumericValue(itemString.charAt(start+1));
            else
                itemCost[k] = Double.parseDouble(itemString.substring(start+1,end));


            i = end+1;
            k++;
            noOfElements++;
        }

    }
    /* extractFromPatter definition,  to extract package and item details from file : Ends Here */


    /* maxCostPackage definition,  to calculate maximus cost of the package : Starts Here */
    public static void maxCostPackage(){
        double[] costToWeight = new double[15]; /* array for cost to weight ratio */
        int temp;
        int i; 
        int j;
        double temp1;

        for( i = 0; i < noOfElements; i++ )
            costToWeight[i] =   itemCost[i] / itemWeight[i];    /* calculation of cost to weight ratio */

        /*sorting of array cost_to_wegith and index in decreasing order : Starts Here*/
        for( i = 0; i < noOfElements; i++){  
            for( j = 1; j < (noOfElements-i); j++){  
                if(costToWeight[j-1] < costToWeight[j]){  
                    //swap elements  
                    temp1 = costToWeight[j-1];  
                    costToWeight[j-1] = costToWeight[j];  
                    costToWeight[j] = temp1;  

                    temp = index[j-1];
                    index[j-1] = index[j];
                    index[j] = temp;
                }  
            }  
        }
        /*sorting of array cost_to_wegith and index in decreasing order : Ends Here*/

        /* declaring and initializing variales for maxCost calculation */
        int flag = 0;
        double sumOfWeight = 0.0;
        double costOfItems = 0.0;
        double tempCost;
        double tempWeight;

        /* max cost calculation : Starts here */
        for( i = 0; i < noOfElements; i++)
        {
            if( ( sumOfWeight + itemWeight[index[i]-1]) < packageWeight ) /* if sum of weight is less than the package weight */
            {

                if(flag == 0)
                {   /* flag = 0 is used for three things... 1). for providing ", " 2). for calculation of future values 3). print "-" if pakcage is empty */
                    System.out.print(index[i]);
                    sumOfWeight = sumOfWeight + itemWeight[index[i]-1];
                    costOfItems = costOfItems + itemCost[index[i]-1]; 

                    /* here index[i]-1 i used because the index of array start from 0, unlike what we have stored in "index" array */
                }
                else
                {
                    tempWeight = sumOfWeight + itemWeight[index[i]-1];
                    tempCost = costOfItems + itemCost[index[i]-1]; 

                    /* index[i+1]-1 is used to check the next value... only then increase the value of i inside the loop */
                    while( (sumOfWeight + itemWeight[index[i+1]-1]) < packageWeight && (costOfItems + itemCost[index[i+1]-1]) > tempCost )
                    {
                        /* if the sum of cost of next item is more than current one, only then move forward to add it */
                        i++;
                        tempWeight = sumOfWeight + itemWeight[index[i]-1];
                        tempCost = costOfItems + itemCost[index[i]-1]; 

                    }
                    sumOfWeight = tempWeight; /* values of temp_ will be assigned, it might come from while loop or outside of it */
                    costOfItems = tempCost;
                    System.out.print(", "+index[i]);
                }
                flag = 1;

            }

        }
        if(flag == 0)
            System.out.println("-"); //
        else
            System.out.println("");
        /* max cost calculation : Ends here */
    }
    /* maxCostPackage definition,  to calculate maximus cost of the package : Ends Here */
}
/* class "Package" where the logic is coded : Ends Here */