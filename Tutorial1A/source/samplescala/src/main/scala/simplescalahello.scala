/**
  * Created by sowmya on 6/10/17.
  */
import fs2.{io, text, Task}

object simplescalahello extends App {

  object Math{
    def add1(e: Int, j: Int) = e+j
  }

  def add(x: Int, y: Int): Int = {

    return x + y

  }

  def subtract(a: Int, b: Int): Int = {

    return a - b

  }
  def mul(m: Int, n: Int): Int = {

    return m * n

  }
  def div(f: Int, t: Int): Int = {

    return f / t

  }



val sum=Math.add1(4,6)
    println(s"sum: ${sum}")
    println(add(12, 46))
    println(subtract(10, 8))
    println(mul(3,8))
    println(div(6,3))

}