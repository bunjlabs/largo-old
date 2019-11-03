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
var environment = new DefaultLargoEnvironment();
environment.export("print", LargoFunction.fromBiConsumer((ctx, str) -> System.out.println(str.asJString())));
environment.export("math", MathLib.LIB);

var runtime = new DefaultLargoRuntime(environment);
var function = runtime.load(new FileReader("test.lgo"));

function.call(environment.getContext());
```

## Examples
#### Hello world
```
import print;

print("Hello world!");
```

#### Fibonacci 
```
import print;

let n = 20;
let a = 0, b = 1, c;

for (let i = 2; i <= n; i++) {
    c = a + b;
    a = b;
    b = c;
}

print(b);
```

#### Math
```
import math as m;
import print;

let age = 38.56;
print(m.floor(age) + " " + m.ceil(age));

let angle = m.toRadians(60);
print(m.sin(angle) + " " + m.cos(angle));
```
