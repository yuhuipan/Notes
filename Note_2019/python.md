print(‘hello,world’)

print(“hello,world”)

 

a = 100

if a >= 0 :

print(a)

else :

print(-a)

 

r’’表示’’内部的字符串默认不转义

‘’’ ‘’’换行

‘’’line1

line2

line3’’’

 

数据类型：

1.整数 1, 100, -800, 0     十六进制用0x前缀和0-9，a-f表示

2.浮点数 3.14  -9.01   123000 = 1.23e5

整数和浮点数在计算机内部存储的方式是不同的，整数运算永远是精确的（除法难道也是精确的？是的！），而浮点数运算则可能会有四舍五入的误差。

3.字符串 ''或""本身只是一种表示方式，r''表示''内部的字符串默认不转义 Python允许用'''...'''的格式表示多行内容

4.布尔值  只有True、False两种值

5.空值  空值是Python里一个特殊的值，用None表示。None不能理解为0，因为0是有意义的，而None是一个特殊的空值。

6.Python还提供了列表、字典等多种数据类型，还允许创建自定义数据类型

 

变量

命名：变量名必须是大小写英文、数字和_的组合，且不能用数字开头

 

常量

PI = 3.14159265359

Python根本没有任何机制保证PI不会被改变，所以，用全部大写的变量名表示常量只是一个习惯上的用法

Python的整数没有大小限制，Python的浮点数也没有大小限制，但是超出一定范围就直接表示为inf（无限大）