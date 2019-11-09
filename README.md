# Largo 
Secure and sandboxed scripting language for Java

## What the hell is it?

Largo is a tiny compiled scripting language that aims to run untrusted scripts in server environment.

### Features
* C/JS-style simplified code
* Dynamically typed without any cast exceptions
* Basic types: null, undefined, boolean, number, string, object, array, function (i.e. closures)
* Compiler / VM architecture
* Linear bytecode including nested functions
* Modules with import/export syntax
* Fully sandboxed and secure
* Configurable constraints for code size, memory, stack, etc.
* Does not have access to external world by default
* Easy embeddable and extensible to Java

### Disadvantages
* Slow due to basic interpreter and security
* Limited in capabilities. No complex programs or it can be harmful
* Buggy and no docs currently (TODO)

## Running scripts
```java
var runtime = LargoRuntime.createDefault();

var environment = runtime.getEnvironment();
environment.addModule("Math", new LargoMathModule());
environment.addModule("System", new LargoSystemModule());

runtime.load("test.lgo");
```

## Examples
#### Hello world
```
import System;

System.println("Hello world!");
```

#### Fibonacci 
```
import System;

let n = 20;
let a = 0, b = 1, c;

for (let i = 2; i <= n; i++) {
    c = a + b;
    a = b;
    b = c;
}

System.println(b);
```

#### Math
```
import Math as M;
import System;

let age = 38.56;
System.println(M.floor(age) + " " + M.ceil(age));

let angle = M.toRadians(60);
System.println(M.sin(angle) + " " + M.cos(angle));
```

### Closure
```
import System;

let add = x -> y -> {
  let z = x + y;
  System.println(x + '+' + y + '=' + z);
  return z;
};

let res = add(3)(6); // will return 9 and print 3+6=9

System.println(res);
```

### Arrays and objects
```
import System;

let array = [5, "str", true];
array[0] = 1;
System.println(array); // print [1, "str", true]

let object = {};
object.field1 = "str";
System.println(object); // print [[object]]
```
