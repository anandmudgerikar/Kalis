public class Module_Manager {

   public static void main(String args[]){
      //char grade = args[0].charAt(0);
      char grade = 'A';	//

      switch(grade)
      {
         case 'A' :
            System.out.println("Print Module working");
            break;
         case 'B' :
        	 System.out.println("Module B working");
         case 'C' :
            System.out.println("Module C working");
            break;
         case 'D' :
        	 System.out.println("Module D working");
         case 'F' :
        	 System.out.println("Module F working");
            break;
         default :
            System.out.println("Invalid option");
      }
   }
}
