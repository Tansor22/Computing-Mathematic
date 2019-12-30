package app

class Test {

    def plus(Test other) {
        println('plus() has been invoked')
    }
    static void main(String[] args) {
        def map = [name: 'Sergey', age: 22]
        Test one = new Test()
        Test another = new Test()
        one + another
        map.age.times {
            print "$it "
        }
        1.upto 10, {
            print "$it "
        }

    }
}
