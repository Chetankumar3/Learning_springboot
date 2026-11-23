# Deep Dive on JVM & Spring Boot

> **Reference:** [https://gemini.google.com/app/4857c5d3e6a7e5da](https://gemini.google.com/app/4857c5d3e6a7e5da)

---

## Singleton Architecture & The Need for IoC

The most standard backend architecture for the codebase is **singleton architecture**, where a business application is divided into multiple services inside the service layer, and services interact by calling functions of each other. In singleton architecture, each service will have exactly one instance.

Now, because a function in a service (let's say `A`) should be able to call a function of another service (let it be `B`), we need to have a reference to the instance of service `B`. Technically, there are two ways to achieve this:

1. **Manual Instantiation:** To create a new instance of service `B` in the constructor of service `A` and hold a reference to it. But this method is against the singleton architecture, because this brings tight coupling between services, due to which multiple instances of some service may be created if multiple services require reference to a specific service.
2. **Centralized Manager (IoC):** To adhere to the singleton architecture, what we need, according to me, intuitively, is: a manager which creates a single object of all services and handovers a reference to that single object to whoever asks for it. And this is exactly what **IoC** design means.

---

## Core Concepts

* **IoC:** A design where external container, runtime, or framework takes over the flow of execution, object lifecycles, and dependency management, and it calls your code when needed.
* **Beans:** In Spring, beans are the objects that form the backbone of your application and are managed by the Spring IoC container. A bean is simply an object that is instantiated, assembled, and otherwise managed by a Spring IoC container. `RequestController`, `Service`, `Repository`, etc. are all examples of beans in a Spring application.
* **Spring IoC Container (`ApplicationContext`):** It is the manager that handles the life cycle of beans and their dependencies. It creates and manages the beans, and injects them into other beans as needed. The Spring IoC container is responsible for instantiating, configuring, and assembling the beans.
* **Dependency Injection:** Dependency Injection (DI) is a design pattern used to implement IoC, allowing the creation of dependent objects outside of a class and providing those objects to a class (give reference) in different ways. In Spring, DI can be achieved through **constructor injection**, **setter injection**, or **field injection**.

---

## What Happens in Constructor Injection?

*Writing a constructor of a bean class `A` with an argument as a service class `B`:*

1. Firstly, during the **class path scan**, Spring Boot identifies that both class `A` and class `B` are beans and stores the `BeanDefinition` (lightweight blueprint of beans).
2. Then, after getting all the bean definitions, Spring uses **Java Reflection** to inspect the constructors of every `BeanDefinition`.
3. Now, because we wrote the constructor of bean `A` with an argument of bean `B`, Spring identifies that bean `A` depends on bean `B`.
4. Like this, Spring first identifies all the dependencies in the application and builds a **dependency tree**.
5. And now it creates all the objects and hands their references to the specific dependents from the **bottom up** of the dependency tree.

---

## Role of `final` Keyword in Dependency Injection

The `final` keyword just acts like a `const` restrictor in C++. Java writers brought the new keyword `final` instead of copying `const` keyword because the `const` of C++ was very complicated due to the west `const` (`const int* p`) and east `const` (`int* const p`). But there are no pointers in Java, which means there is no west `const`. So, `final` in Java is equal to east `const` in C++. And because the same keyword doesn't bring the same meaning, Java writers avoided copying the keyword.

---

## How Do 10,000 Requests Run on 1 Singleton Object in Parallel?

One singleton object means that there is only one place where methods of those objects are stored as machine code instructions. Each request uses the same machine instructions with their own program counter to execute the methods.

### What happens when we call `serviceA.func()`:

1. The JVM first gets the variable reference of `serviceA` variable. Through that, it gets the object reference (reference of **Heap**) of service `A`.
2. Then it reaches **`Heap`** and gets the **Klass pointer** (reference of **Metaspace** memory) from its object's header.
3. Then it reaches **`Metaspace`** and gets the reference for `Method` metadata structure from `vtable` of service `A`.
4. Still in **`Metaspace`**, it gets the reference of the machine code instructions of the method `func()` from the `Method` metadata structure.
5. Then it reaches **`Code Cache`** and gets the machine code instructions of the method.

Each request is a thread and each thread has its own **stack** and **program counter**. When the Tomcat server receives the request, it allocates a thread to it and inside that thread, the controller method is called (the method that was mapped to the request URL). And inside that controller, when a service function is called, the above 5 steps are followed to get the reference of the machine code instructions of the service function and then execute the method.

So, note that the **machine code instructions are not copied inside the thread of the request**. Each thread uses the same machine code instructions with its own program counter to execute the method. So, if 10,000 requests are made to a singleton service, all 10,000 threads will use the same machine code instructions of the service methods to execute them in parallel.

> **Note (Java 21+ Virtual Threads):** In Java 21+, virtual threads come into place. So instead of the traditional framework, where the framework requests the OS for each request to get the thread and allocates that thread to the request, the framework will allocate a virtual thread to each request. And the virtual thread is a lightweight thread that is managed by the JVM instead of the OS. Basically, JVM carves out the memory inside the heap which was given by the OS.

---

## `Code Cache`

Every application has its own text/code segment, but Java itself isn't an entire application—it's a wrapper around a C++ application. When we run the Java application, the OS already hands out the text segment, in which machine code instructions of the JVM itself live on.

And when JVM runs, it first asks OS for some dynamic memory segments:

* **JVM Heap**
* **Metaspace**
* **Code Cache**

It also asks the OS to make the **Code Cache** memory segment **executable**. And then JVM loads the machine code instructions of the methods of the application into the `Code Cache` memory segment.

---

## When Does a Singleton Become a Bottleneck (or a Bug)?

When a singleton service has a **shared mutable state** (like a class variable) that is accessed and modified by multiple threads, it can lead to race conditions and inconsistent behavior. For example, consider the following code:

```java
@Service
public class BillingService {
    private double tempDiscount; // DANGER! Shared on the Heap across all threads!

    public double calculateBill(double price) {
        this.tempDiscount = price * 0.1; // Thread A writes, Thread B overwrites!
        return price - this.tempDiscount;
    }
}

```

If you do this, you get a classic **race condition**. And if you try to "fix" it by slapping `synchronized` (a mutex) on the method, then you have created the exact sequential bottleneck you were worried about.

> **The Golden Rule of Spring Beans:** Keep Singleton Beans **stateless** (store per-request data in local variables/method parameters on the Stack, never in class fields on the Heap).