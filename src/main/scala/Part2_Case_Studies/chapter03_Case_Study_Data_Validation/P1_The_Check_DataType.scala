package Part2_Case_Studies.chapter03_Case_Study_Data_Validation

object P1_The_Check_DataType {

  def main(args: Array[String]): Unit = {

//    type Check[E, A] = A => Either[E, A]

    trait Check[E, A] {
      def apply(value: A): Either[E, A]
    }
  }
}
