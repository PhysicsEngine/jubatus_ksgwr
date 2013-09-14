## GettingStart

### install homebrew

$ ruby -e “$(curl -fsSkL raw.github.com/mxcl/homebrew/go)”  

http://bushi.6.ql.bz/?p=895

### brew doctor

brew doctorで警告が出ている場合、gccが古いなどの警告が出てインストールできない場合があるので
回避しておく

Warning: Experimental support for using Xcode without the "Command Line Tools". 　　

恐らくこれがエラー原因。  

Xcodeを起動し、Preference > Downloads を開く。
Command Line Tools をインストールする。  

http://blog.pinkpinkpink.net/2012/07/homebrew.html

### install jubatus

$ brew tap jubatus/jubatus
$ brew install --HEAD pficommon
$ brew install jubatus

https://github.com/jubatus/homebrew-jubatus

jubatusと打ってTabの補間が効けばパスが通っているのでインストールは成功です。
