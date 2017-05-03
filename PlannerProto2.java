// PlannerProto2.java
// Yet another Java program exploring the possibility of computer-aided
//+planning in Minecraft/IC2
// Christian Bikle
// 04/26/2017

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class PlannerProto2 {
//===CONSTANTS===
   static final String CONTAINS = ">";
   static final String NUM = "=";
   static final String MIN = "/";
   static final String AND = "&";
   static final String OR = "|";
//===VARIABLES===
   ArrayList<Part> items;

//===MAIN===
   public static void main(String[] args) {
      //println("Hello world");
      
      PlannerProto2 pp2 = new PlannerProto2("pp2data.txt");
/*       pp2.simplePartPrint(pp2.lookupPart("BatBox", pp2.getItems()));
      pp2.simpleCalc(pp2.lookupPart("BatBox", pp2.getItems()),4);
      pp2.simpleCalc(pp2.lookupPart("Stick", pp2.getItems()),3);
      pp2.simpleCalc(pp2.lookupPart("TinCable", pp2.getItems()),5); */
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),1);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),2);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),3);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),4);//1
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),5);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),6);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),7);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),8);//2
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),9);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),10);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),11);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),12);//3
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),13);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),14);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),15);
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),16);//4
      pp2.simpleCalc(pp2.lookupPart("Plank", pp2.getItems()),17);//5
   }
//===CONSTRUCTORS===
   public PlannerProto2(String s) {
      try {
         this.items = loadFile(s);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
   
   private ArrayList<Part> loadFile(String filename) throws IOException {
      ArrayList<Part> newItems = new ArrayList<>();
      
      BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
      
      String line;
      while((line = br.readLine()) != null) {
         //println(line);
         if(line.length() > 0 && line.charAt(0) != '#') {
            newItems.add(parseLine(line, newItems));
         }
      }
      
      return newItems;
   }
//===SIMPLE TRAVERSE===
   private void simpleTraverse(ArrayList<Part> items) {
       items.forEach((p) -> {
           simplePartPrint(p);
       });
   }
//===SIMPLE PART PRINT===
   private void simplePartPrint(Part p) {
      println(p.getName()+" ("+p.getMin()+"):");
      if(p.getSubParts() != null) {
         Subpart[] subParts = p.getSubParts();
         for (Subpart subPart : subParts) {
            println(" " + subPart.getPart().getName() + " x " + (int) subPart.getNumOfPart());
         }
      } else {
         println(" null");
      }
   }
//===SIMPLE CALC===
   private void simpleCalc(Part p, int n) {
      int actualN = 0;
      
      int min = p.getMin();
      if(n > min) {
         actualN = n / (min+1);
         println(actualN+"");
      } else {
         actualN = 0;
      }
      println(p.getName()+" ("+n+"):");
      if(p.getSubParts() != null) {
         
         Subpart[] subParts = p.getSubParts();
         
         for (Subpart subPart : subParts) {
            //double sn = numSubParts[i] * n
            println(" " + subPart.getPart().getName() + " x " + (min * subPart.getNumOfPart() + actualN)); //<<<fix this
         }
      } else {
         println(" null");
      }
   }
   
//===PARSELINE===   
   private Part parseLine(String line, ArrayList<Part> items) {
      Part p = new Part();
      if(!line.contains(CONTAINS)) {
         p.setName(line);
      } else {
         String[] nameEtc = line.split(CONTAINS);
         p.setName(nameEtc[0]);
         String[] minEtc = nameEtc[1].split(MIN);
         p.setMin(Integer.parseInt(minEtc[1]));
         String[] subPartsAndNums = minEtc[0].split(AND);
         // TODO: implement OR
         
         Subpart[] subParts = new Subpart[subPartsAndNums.length];
         
         for(int i = 0; i < subPartsAndNums.length; i++) {
            String[] subPartAndNum = subPartsAndNums[i].split(NUM);
            subParts[i].setPart(lookupPart(subPartAndNum[0], items));
           subParts[i].setNumOfPart(Double.parseDouble(subPartAndNum[1]));
         }
         p.setSubParts(subParts);
      }
      
      return p;
   }
//===LOOKUP PART===// TODO: what if lookup fails?
   private Part lookupPart(String target, ArrayList<Part> items) {
      for(Part p : items) {
         if(p.getName().equals(target)) {
            return p;
         }
      }
      return null;
   }
   
//===ROUNDUP===
   public static int roundUp(double d) {
      return (int)(d+0.5);
   }
//===PRINT===
   public static void println(String s) {
      print(s+"\n");
   }
   
   public static void print(String s) {
      System.out.println(s);
   }
   
//===GETTERS===
   public ArrayList<Part> getItems() {
      return this.items;
   }
   
//===PART CLASS===
   public class Part {
//===VARIABLES===
      String name;
      Subpart[] subParts;
      int min;
//===CONSTRUCTORS===
      public Part() {
         
      }
      
      public Part(String name, Subpart[] subParts, int min) {
         this.name = name;
         this.subParts = subParts;
         this.min = min;
      }
//===TOSTRING===
      @Override
      public String toString() {
         return name;
      }
//===GETTERS===
      public String getName() {
         return name;
      }
      public Subpart[] getSubParts() {
         return subParts;
      }
      public int getMin() {
         return min;
      }
//===SETTERS===
      public void setName(String name) {
         this.name = name;
      }
      public void setSubParts(Subpart[] subParts) {
         this.subParts = subParts;
      }
      public void setMin(int min) {
         this.min = min;
      }
   }

//===SUBPART CLASS===
   public class Subpart {
//===VARIABLES===
      Part p;
      double numOfPart;
//===CONSTRUCTORS===
      public Subpart() {

      }

      public Subpart(Part p, double n) {
         this.p = p;
         this.numOfPart = n;
      }
//===GETTERS===
      public Part getPart() {
         return p;
      }
      public double getNumOfPart() {
         return numOfPart;
      }
//===SETTERS===
      public void setPart(Part p) {
         this.p = p;
      }
      public void setNumOfPart(double n) {
         this.numOfPart = n;
      }
//===TOSTRING===
      @Override
      public String toString() {
         return p.getName();
      }
   }
}
