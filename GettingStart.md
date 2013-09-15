## GettingStart

MacでJubatus環境を構築しよう  

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
$ brew install jubatus --enable-re2  
(オプションを指定しないとチュートリアルが動きません)  
($ brew options jubatusで指定できるオプションを調べることができます)  

https://github.com/jubatus/homebrew-jubatus

jubatusと打ってTabの補間が効けばパスが通っているのでインストールは成功です。

### Tutorial

#### Python環境構築

公式TutorialはPythonが例になっているためPython用のセッティングを行います

$ sudo easy_install pip  
$ sudo pip install jubatus  

http://jubat.us/ja/quickstart.html

#### 公式Tutorial

ニュースの投稿内容から、投稿先ニュースグループを推測することをチュートリアルで行います。

/usr/local/Cellar/jubatus にインストールされています。



必要があればwgetもinstallしておきます

$ brew install wget  

$ git clone https://github.com/jubatus/jubatus-tutorial-python.git  
$ cd jubatus-tutorial-python  
$ wget http://people.csail.mit.edu/jrennie/20Newsgroups/20news-bydate.tar.gz  
$ tar xvzf 20news-bydate.tar.gz  
$ jubaclassifier --configpath config.json  

(以下は--enable-re2のオプションが指定されてないと出るエラーです)  
I0914 20:31:18.261653 2061109600 server_util.cpp:71] load config from local file: config.json  
F0914 20:31:18.264600 2061109600 server_util.hpp:144] Dynamic exception type: jubatus::fv_converter::converter_exception::what: unknown filter name: regexp

サーバーが立ち上がります。次はクライアントを立ち上げるため新規ターミナルを立ち上げましょう  

$ python tutorial.py  
クライアントの実行です。同ディレクトリのデータをサーバに投げ結果を出力します  
テスト結果 正解　実際の値　スコア  
が出力されます。

http://jubat.us/ja/tutorial.html
