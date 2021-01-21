import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /** User Input ####################################################*/

        /**user input for dimension N*/
        int  N=Integer.parseInt(scanner.nextLine());
        /** Checking if N is withing specs*/
        while(N<=1&&N>100&&N%2==0){
            System.out.println("-1 N shoud be an even non-negative number below 100!");
        }

        /**user input for dimension M*/
        int  M=Integer.parseInt(scanner.nextLine());
        /** Checking if M is withing specs*/
        while(N<=1&&N>100&&N%2==0){
            System.out.println("-1 M shoud be an even non-negative number below 100!");
        }

        /** Creating an array to store the information*/
        int[][] arr=new int[N][M];
        /** calculating the max number of elements for given dimensions*/
        int max=(N*M)/2;

        /** Loop for inputting the initial layout of the bricks */
        for(int i=0;i<N;i++){

            /** each row is collected separately in this variable*/
            String row=scanner.nextLine();

            /** checking if number of elements inputted is correct*/
            while(row.chars().filter(num->num==' ').count()!=M-1){
                System.out.println("-1 You have to enter "+M+" elements on each row!");
                row=scanner.nextLine();
            }

            /** variable for keeping track of second dimension of array*/
            int m=0;

            /** Loop for separating each element and converting them from string to integer*/
            for(String a: row.split(" ")){
                arr[i][m]=Integer.parseInt(a);
                m++;
            }
        }
        /**#############################################################*/

        /** creating an array for keeping track of wether brick numbers have already been used*/
        int[] av=new int[max];
        /** intializing the array with numbers corresponding to their index*/
        for(int i=0; i<max; i++){
            av[i]=i+1;
        }

        /** checking wether all brick size is correct*/
        for(int a:av) {
            int sum=0;
            for (int i = 0; i < arr.length; i++) {
                for (int s = 0; s < arr[0].length; s++) {
                    if (a==arr[i][s]){
                        sum++;
                    }
                }
            }
            if (sum>2){
                System.out.println("-1 All bricks size should be 1x2!");
            }
        }

        /** calling custom function for reorder*/
        int[][] arr2=reorder(arr,av);

        /** calling custom function for printing*/
        print(arr2);
    }

    /** Custom function for reordering the bricks*/
    static int[][] reorder(int[][] arr, int[] av){

        /** array for storing the new arrangement*/
        int[][] arr2=new int[arr.length][arr[0].length];

        /** 2 Loops for going through the initial array*/
        for (int i=0;i<arr.length;i+=2) {
            for(int k=0;k<arr[0].length; k++) {

                /** The concept is to check the current brick number, and then choose a different one,
                 * so as to not have overlaping same brick in the new layout,
                 * we're chosing a number by adding or subtracting from the current
                 * one and then checking if the number hasn't already been used */

                /** variable for keeping track of current iteration, allowing us to change between addition and subraction when looking for different nubers*/
                int iteration=0;

                /** variable for storing previous element*/
                int a=0;

                do {
                    /** Variable for checking if iteration is even of odd number, in order to change between addition and subtraction*/
                    boolean evenodd=iteration%2==0;
                    /** Variable for checking wether there is higher element number*/
                    boolean boundHigh=(arr[i][k]+(iteration/2))<av.length;
                    /** Variable for checking wether there is lower element number*/
                    boolean boundLow=((arr[i][k]-((iteration/2)+1))-1)>=0;
                    /** Variable for checking wether there is lower element number*/
                    boolean subcheck=(arr[i][k]-((iteration/2)+1))>=0;


                    /** First case: trying higher number*/
                    /** last entry in the logical expression is checking wether higher number isn't already used*/
                    if (evenodd && boundHigh && av[arr[i][k]+(iteration/2)]!=0) {

                        /** Storing current element for compariosn in next iteration*/
                        a=av[arr[i][k]+(iteration/2)];
                        /** Setting the different numbered brick in the second array*/
                        arr2[i][k] = av[arr[i][k]+(iteration/2)];
                        arr2[i + 1][k] = av[arr[i][k]+(iteration/2)];

                        /** Setting the state of currently used number to unavailable, the 'av' array contains, all possible numbers for bricks.*/
                        av[arr[i][k]+(iteration/2)] = 0;

                        /** same operations used in the other cases */

                    }/** Second case: trying lower number*/
                    /** last entry in the logical expression is checking wether lower number isn't already used*/
                    else if(subcheck&&av[(arr[i][k]-((iteration/2)+1))]!=0){

                        /** Fringe cases when current element number is the only one left to put into the position*/
                        if(evenodd && boundLow){

                            a=av[(arr[i][k]-((iteration/2)+1))-1];
                            arr2[i][k] = av[(arr[i][k]-((iteration/2)+1))-1];
                            arr2[i + 1][k] = av[(arr[i][k]-((iteration/2)+1))-1];
                            av[(arr[i][k]-((iteration/2)+1))-1] = 0;

                        }else {

                            a=av[(arr[i][k]-((iteration/2)+1))];
                            arr2[i][k] = av[(arr[i][k]-((iteration/2)+1))];
                            arr2[i + 1][k] = av[(arr[i][k]-((iteration/2)+1))];
                            av[(arr[i][k]-((iteration/2)+1))] = 0;

                        }
                    } iteration++;

                } /**Checking wether, an available number has been picked*/ while (a==0);
            }
        } return arr2;
    }

    /** Custom function for printing the layout in the expected way*/
    static void print(int[][] arr){

        /**initial line of asterix' on top*/
        for(int l=0;l<=(arr[0].length*2);l++){
            System.out.printf("%1.5s","*");
        } System.out.println();
        /**###################*/

        /** Variable for keeeping track of first array dimension */
        int g=0;

        /** List of integers, keeping track of elements with same number, in order to differentiate the bricks*/
        ArrayList<Integer> flag=new ArrayList<>();

        /** Loop going through array*/
        for (int[] a:arr) {

            /**variable for keeping track of second array dimension*/
            int h=0;

            /** Asterix at the beginning of every brick line*/
            System.out.printf("*");

            /** Loop for going through the inner array*/
            for (int b:a) {

                /** variables for checking if adjacent element is part of the same brick*/
                boolean next=((h+1)<arr[0].length && arr[g][h]==arr[g][h+1]);  /**next element*/
                boolean below= ((g+1)<arr.length && arr[g][h]==arr[g+1][h]);  /**lower element*/

                /** elements from the same brick are connected with a dash, as opposed to elements from different bricks*/
                if(next==true){
                    System.out.printf(b+"-");
                }else{
                    System.out.printf(b+"*");
                }

                /** keeping track of the position of elements, having adjacent element from the same brick, below the current line*/
                if(below==true) {
                    flag.add(h);
                }

                h++;
            } /** separator*/System.out.println();

            /** Checking wether we have bricks with vertical orientation*/
            if(flag.size()!=0) {

                /**Variable for keeping track of elements of same brick, in the List*/
                int o=0;

                /** Loop for printing boundaries between elements of same,
                 * and of different bricks, boundary between different bricks
                 * is done by using an Asterix [*],
                 * between the same brick using a Dash [-].
                 * The number of symbols on a row, turns out to be,
                 * twice the number of elements in it, that's why
                 * the variable 'l' in the loop is bounded by
                 * the size of the array's row multiplied by 2*/

                for (int l = 0; l <= (arr[0].length * 2); l++) {

                    /** Checking if the index of position between numbers of the same brick,
                     * saved in the List, corresponds to the proper place for visualization,
                     * if it does, a Dash [-] is used, if not an Asterix [*] ,
                     * also formatting the output so that every symbol uses the same amount of space*/
                    if(o<flag.size()&&(flag.get(o)+1+(1*o))==l) {
                        System.out.printf("-");
                        o++;
                    }else {
                        System.out.printf("*");
                    }
                }
                /**Clearing the List*/
                flag.removeAll(flag);
                System.out.println();

            } /** Case where, no vertically positioned bricks are found, using only Asterix [*] */ else {
                for(int l=0;l<=(arr[0].length*2);l++){
                    System.out.printf("%1.5s","*");
                } System.out.println();
            }
            g++;
        }

    }
}


