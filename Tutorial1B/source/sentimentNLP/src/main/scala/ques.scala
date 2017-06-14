import scala.io.Source


object ques1 {
  def main(args: Array[String]) {

    val filename = "file2.txt"
    for (line <- Source.fromFile(filename).getLines()) {

      if(line == "Where the dollar reached $1.2871 against the euro."){
        System.out.println("Where the dollar reached $1.2871 against the euro?")
        System.out.println("NEWYORK")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")


      }
      else if(line == "When Federal Reserve's decision boosted.") {
        System.out.println("When Federal Reserve's decision boosted?")
        System.out.println("In the meantime, the US Federal Reserve's decision on 2 February to boost interest rates by a quarter of a point")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")


      }
      else if (line == "Who will announce the budget on Monday.")
      {
        System.out.println("Who will announce the budget on Monday?")
        System.out.println("The White House will announce its budget on Monday, and many commentators believe the deficit will remain at close to half a trillion dollars..")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")
        System.out.println("        ")




      }
      else {
        System.out.println("not found")
      }
    }
  }
}
