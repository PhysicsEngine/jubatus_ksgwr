  import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;

  public class Shogun {
   public static final String HOST = "127.0.0.1";
   public static final int PORT = 9199;
   public static final String NAME = "shogun";

   String[] data1 = {
           "家康", "秀忠", "家光", "家綱", "綱吉",
           "家宣", "家継", "吉宗", "家重", "家治",
           "家斉", "家慶", "家定", "家茂"
   };
   String[] data2 = {
           "尊氏", "義詮", "義満", "義持", "義量",
           "義教", "義勝", "義政", "義尚", "義稙",
           "義澄", "義稙", "義晴", "義輝", "義栄"
   };
   String[] data3 = {
           "時政", "義時", "泰時", "経時", "時頼",
           "長時", "政村", "時宗", "貞時", "師時",
           "宗宣", "煕時", "基時", "高時", "貞顕"
   };

   private final void start() throws Exception {
           // 1.Jubatus Serverへの接続設定
           ClassifierClient client = new ClassifierClient(HOST, PORT, 5);

           // 2.学習用データの準備
           List<TupleStringDatum> studyData = this.makeStudyData();

           // 3.データの学習（学習モデルの更新）
           client.train(NAME, studyData);

           // 4.予測用データの準備
           List<Datum> exData = this.makeExData();

           // 5.学習データに基づく予測
           List<List<EstimateResult>> result = client.classify(NAME, exData);

           // 6.結果の出力
           this.output(exData, result);

           return;
   }

   // 2.学習用データの準備
   private final List<TupleStringDatum> makeStudyData() {
           List<TupleStringDatum> result = new ArrayList<TupleStringDatum>();
           String familyName = "";

           // ループ処理にて、各将軍の姓と名のセットを作成
           for (int i = 0; i < 3 ; i++) {
                   String[] nameList = null;
                   switch (i) {
                   case 0:
                           familyName = "徳川";
                           nameList = this.data1;
                           break;
                   case 1:
                           familyName = "足利";
                           nameList = this.data2;
                           break;
                   case 2:
                           familyName = "北条";
                           nameList = this.data3;
                           break;
                   }

                   for (String name : nameList) {
                           TupleStringDatum train = new TupleStringDatum();

                           // datumを作成
                           Datum datum = new Datum();
                           datum.string_values = new ArrayList<TupleStringString>();
                           datum.num_values = new ArrayList<TupleStringDouble>();

                           // インスタンス変数firstにkey、secondにvalueを格納
                           TupleStringString tss = new TupleStringString();
                           tss.first = "name";
                           tss.second = name;

                           datum.string_values.add(tss);

                           train.first = familyName;
                           train.second = datum;

                           result.add(train);
                   }
           }
           // 学習用データをシャッフル
           Collections.shuffle(result);

           return result;
   }

   // 4.予測用データの準備
   private List<Datum> makeExData() {
           List<Datum> result = new ArrayList<Datum>();

           String name = null;
           for (int i = 0; i < 3; i++) {
                   switch (i) {
                   case 0:
                           name = "慶喜";
                           break;
                   case 1:
                           name = "義昭";
                           break;
                   case 2:
                           name = "守時";
                           break;
                   }

                   // datumを作成
                   Datum datum = new Datum();
                   datum.string_values = new ArrayList<TupleStringString>();
                   datum.num_values = new ArrayList<TupleStringDouble>();

                   TupleStringString tss = new TupleStringString();

                   // インスタンス変数firstにkey、secondにvalueを格納
                   tss.first = "name";
                   tss.second = name;

                   datum.string_values.add(tss);

                   result.add(datum);
           }
           return result;
   }

   private void output(List<Datum> exData, List<List<EstimateResult>> result) {
           // 結果の出力
           int i = 0;
           int j = 0;
           int iMax = 0;
           double max = 0;
           for (List<EstimateResult> res : result) {
                   // 結果リストの中でscoreが最大のものを判定
                   for (j = 0; j < res.size(); j++) {
                           if (res.get(j).score > max || max == 0) {
                                   max = res.get(j).score;
                                   iMax = j;
                           }
                   }
                   // 結果表示
                   System.out.print(res.get(iMax).label + " "
                                   + exData.get(i).string_values.get(0).second + "\n");
                   max = 0;
                   i++;
           }
           System.out.println();
   }

   public static void main(String[] args) throws Exception {
           new Shogun().start();
           System.exit(0);
   }
  }