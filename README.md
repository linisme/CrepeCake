<p align="center"><a href="https://github.com/MEiDIK/CrepeCake" target="_blank"><img width="200"src="logo.png"></a></p>
<h1 align="center">CrepeCake</h1>
<p align="center">A compile-time aop engine like AspectJ but easier to use in android application development.</p>
<p align="center">
  <a href='https://bintray.com/idik-net/CrepeCake/api/_latestVersion'><img src='https://api.bintray.com/packages/idik-net/CrepeCake/api/images/download.svg'></a>
  <a href="https://github.com/MEiDIK/CrepeCake/blob/master/LICENSE"><img src="https://img.shields.io/github/license/MEiDIK/CrepeCake.svg" alt="GitHub license"></a>
  <a href="#"><img src="https://img.shields.io/badge/Recommend-%E2%AD%90%EF%B8%8F%E2%AD%90%EF%B8%8F%E2%AD%90%EF%B8%8F%E2%AD%90%EF%B8%8F%E2%AD%90%EF%B8%8F-green.svg" alt="Recommend"></a>
</p>


## Wiki

  * [中文](https://github.com/MEiDIK/CrepeCake#%E5%85%88%E6%92%87%E4%B8%80%E7%9C%BC--)

  * [English](https://github.com/MEiDIK/CrepeCake#first-at-a-glance--)

-----

## First At A Glance : ]

Here is am example that injected the ``onCreate()`` Method in ``MainActivity``:

```Java

@Aspect(MainActivity.class)
public class MainActivityAspect {

    protected void onCreate(InvocationHandler invocationHandler, Bundle savedInstanceState) {
      System.out.println("⇢ onCreate");
      long startTime = System.currentTimeMillis();
      invocationHandler.invoke(savedInstanceState);
      System.out.println(String.format("⇠ onCreate [%dms]", System.currentTimeMillis() - startTime));    
    }

}

```

That's all，these code will be executed during the onCreate method in MainActivity with printing the running time of it:

```
I/System.out: ⇢ onCreate
I/System.out: ⇠ onCreate [33ms]
```



## Installation

1. Add the buildscript dependencies in the root project:

   ```Groovy
   buildscript {
 
     repositories {
         google()
         jcenter()
     }
     dependencies {
         classpath 'com.android.tools.build:gradle:3.0.1'
         classpath 'net.idik.crepecake:plugin:0.0.3' // Add Here
     }
 
   }
   ```

2. Add crepecake plugin and dependencies in the target module
   > The crepecake plugin MUST **BEFORE**(**VERY IMPORTANT**) the application plugin

   ```Groovy
   apply plugin: 'net.idik.crepecake'
 
   dependencies {
       implementation 'net.idik.crepecake:api:0.0.3'
       annotationProcessor 'net.idik.crepecake:compiler:0.0.3'
       // Other dependencies...
   }
 
   ```

## Usage

1. Programme the injection processor on the basis of target class, with the annotation ``@Aspect``

   ```Java
   @Aspect(MainActivity.class)
   public class MainActivityAspect {
     // injection methods...
   }
   ```

2. Programme the injection method

   Step 1. Declare the injection method with the target method infos, including the access level, static or not, name of method, parameters.

   Step 2. Insert ``InvocationHandler invocationHandler`` at the 1st position of the parameter list

   Done.

   ```Java
   @Aspect(MainActivity.class)
   public class MainActivityAspect {

       protected void onCreate(InvocationHandler invocationHandler, Bundle savedInstanceState) {
         System.out.println("⇢ onCreate");
         long startTime = System.currentTimeMillis();
         invocationHandler.invoke(savedInstanceState);
         System.out.println(String.format("⇠ onCreate [%dms]", System.currentTimeMillis() - startTime));
       }

       public android.support.v7.app.ActionBar getSupportActionBar(InvocationHandler invocationHandler) {
           ActionBar bar = (ActionBar) invocationHandler.invoke();
           //do stuff...
           return bar;
       }

       //other injection methods...
   }
   ```

So far, we have completed the job that injects the ``onCreate(Bundle savedInstanceState)``，``getSupportActionBar()`` and other methods in the MainActivity.

> ``InvocationHandler`` is a flag indicating this is an injection method. Beside, we could invoke the origin method through it and obtain the result(if return type is not ``void``).

#### AspectConfig

We could customize the injection points by inheriting the ``AspectConfig`` class

```Java
public class OnClickAspectConfig extends AspectConfig {
    @Override
    protected boolean isEnable() {
        return super.isEnable();
    }

    @Override
    public boolean isHook(Class clazz) {
        return View.OnLongClickListener.class.isAssignableFrom(clazz) || View.OnClickListener.class.isAssignableFrom(clazz);
    }
}

```

The above codes means that we will inject **ALL** the subClass of ``OnLongClickListener`` and ``OnClickListener``.

> ⚠️ Attention: The AspectConfig class will be executed during the compile-time, so not do running-time logic within it.

Then, use it to the injection processor with the annotation ``@Aspect``

```Java
@Aspect(OnClickAspectConfig.class)
public class OnClickListenerAspect {
  public void onClick(InvocationHandler invocationHandler, View view) {
      System.out.println("OnClick: " + view);
      invocationHandler.invoke(view);
  }

  public boolean onLongClick(InvocationHandler invocationHandler, View view) {
      boolean isConsume = (boolean) invocationHandler.invoke(view);
      if (isConsume) {
          System.out.println("OnLongClick: " + view);
      }
      return isConsume;
  }
}
```

## See more...

* [HelloHugoCake](https://github.com/MEiDIK/HugoCake) - A "hugo" implement with Crepecake aop engine.

## Great Thanks to...

* [Hugo](https://github.com/JakeWharton/hugo) By [JakeWharton](https://github.com/JakeWharton)




---
## 先撇一眼 : ]

举个例子注入MainActivity的onCreate方法:

```Java

@Aspect(MainActivity.class)
public class MainActivityAspect {

    protected void onCreate(InvocationHandler invocationHandler, Bundle savedInstanceState) {
      System.out.println("⇢ onCreate");
      long startTime = System.currentTimeMillis();
      invocationHandler.invoke(savedInstanceState);
      System.out.println(String.format("⇠ onCreate [%dms]", System.currentTimeMillis() - startTime));    
    }

}

```

这就完成了对MainActivity的注入，以上代码会输出onCreate的运行时间：

```
I/System.out: ⇢ onCreate
I/System.out: ⇠ onCreate [33ms]
```



## 安装

1. 在root project的buildscript中添加dependencies如下：

   ```Groovy
   buildscript {
 
     repositories {
         google()
         jcenter()
     }
     dependencies {
         classpath 'com.android.tools.build:gradle:3.0.1'
         classpath 'net.idik.crepecake:plugin:0.0.3' //添加在此
     }
 
   }
   ```

2. 在目标模块的build.gradle文件中添加插件(**请务必添加于Application插件前**)以及依赖如下：
   ```Groovy
   apply plugin: 'net.idik.crepecake'
 
   dependencies {
       implementation 'net.idik.crepecake:api:0.0.3'
       annotationProcessor 'net.idik.crepecake:compiler:0.0.3'
       // 其他依赖...
   }
 
   ```

## 用法

1. 根据目标类，编写注入处理器，通过``@Aspect``指定目标类

   ```Java
   @Aspect(MainActivity.class)
   public class MainActivityAspect {
     // 注入方法们...
   }
   ```

2. 编写注入方法，步骤如下

   Step 1. 声明注入方法与目标方法声明一致，包括访问属性、静态、方法命名、参数列表等  
   Step 2. 添加``InvocationHandler invocationHandler``至参数列表第一位  
   Done.

   ```Java
   @Aspect(MainActivity.class)
   public class MainActivityAspect {

       protected void onCreate(InvocationHandler invocationHandler, Bundle savedInstanceState) {
         System.out.println("⇢ onCreate");
         long startTime = System.currentTimeMillis();
         invocationHandler.invoke(savedInstanceState);
         System.out.println(String.format("⇠ onCreate [%dms]", System.currentTimeMillis() - startTime)); 
       }

       public android.support.v7.app.ActionBar getSupportActionBar(InvocationHandler invocationHandler) {
           ActionBar bar = (ActionBar) invocationHandler.invoke();
           //do stuff...
           return bar;
       }

       //other inject methods...
   }
   ```

至此，我们已经完成了对MainActivity的``onCreate(Bundle savedInstanceState)``，``getSupportActionBar()``以及其他方法的切面注入。

> InvocationHandler这个参数很重要，这个参数标志着这是一个注入方法，并且通过这个参数，我们可以调用目标方法，并获取返回值（如果非``void``）

#### AspectConfig

通过继承``AspectConfig``对象可以对注入点进行个性化的定制，如下：
```Java
public class OnClickAspectConfig extends AspectConfig {
    @Override
    protected boolean isEnable() {
        return super.isEnable();
    }

    @Override
    public boolean isHook(Class clazz) {
        return View.OnLongClickListener.class.isAssignableFrom(clazz) || View.OnClickListener.class.isAssignableFrom(clazz);
    }
}

```

其中，isHook方法判断该类是否为目标类，在上述代码中指定了``OnLongClickListener``和``OnClickListener``的**所有子类**都为注入目标，我们可以通过该方法进行定制。

> ⚠️ 注意：这个类将会在编译时被调用，请不要在此类中做运行时动态逻辑。

写完配置类后，在注入类上通过``@Aspect``注解应用即可

```Java
@Aspect(OnClickAspectConfig.class)
public class OnClickListenerAspect {
  public void onClick(InvocationHandler invocationHandler, View view) {
      System.out.println("OnClick: " + view);
      invocationHandler.invoke(view);
  }

  public boolean onLongClick(InvocationHandler invocationHandler, View view) {
      boolean isConsume = (boolean) invocationHandler.invoke(view);
      if (isConsume) {
          System.out.println("OnLongClick: " + view);
      }
      return isConsume;
  }
}
```

## 查看更多...

* [HelloHugoCake](https://github.com/MEiDIK/HugoCake) - 一个基于CrepeCake的Hugo简易实现

## 万分感谢

* [Hugo](https://github.com/JakeWharton/hugo) By [JakeWharton](https://github.com/JakeWharton)

-----------


## License

    Copyright 2017 认真的帅斌

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
