import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class yq {
    static String parameter = null;
    static String[] parameters = null;
    static int parameterLength = 0;
    static int provinceNumber = 0;
    //加载文件
    public static String loadFile(String[] parameters)throws Exception{
        FileInputStream fip = new FileInputStream(new File("src/"+parameters[0]));//文件输入流
        InputStreamReader reader = new InputStreamReader(fip, "GBK");
        StringBuffer sb1 = new StringBuffer();//读取文件
        while (reader.ready()) sb1.append((char) reader.read());
        String str = sb1.toString();
        return str;
    }
    //处理文件
    public static String[][][] operateFile(String str){
        String[] str1 = str.split("\\s+");
        int str1length = str1.length;
        for (int i = 0;i < str1length;i++) {//处理待明确地区
            i++;
            if(str1[i].equals("待明确地区")){
                str1[i-1] = "*";
                str1[i] = "*";
                i++;
                str1[i] = "*";
            }
            else i++;
        }
        List<String> tmp = new ArrayList<String>();
        for(String item:str1){  //循环数据
            if(item!="*"){  //把不为*的字符串存放到list里（去掉了待明确地区）
                tmp.add(item);
            }
        }
        str1 = tmp.toArray(new String[0]);
        str1length = str1.length;
        String province = "";
        provinceNumber = 0;
        for (int i = 0;i < str1length;i++) {//统计省数
            if(str1[i].equals(province)) i++;
            else {
                province = str1[i];
                provinceNumber++;
                i++;;
            }
            i++;
        }
        province = "阿里嘎多";
        int provinceIndex = -1;
        int countryIndex = -1;
        String data[][][] = new String[provinceNumber][30][2];
        for (int i = 0;i < str1length;i++) {//将文件数据封装到三维数组
            if(str1[i].equals(province)){
                i++;
            }
            else{
                province = str1[i];
                countryIndex =0;
                provinceIndex++;
                data[provinceIndex][countryIndex][0] = str1[i];
                data[provinceIndex][countryIndex][1] = "0";
                i++;
            }
            countryIndex++;
            data[provinceIndex][countryIndex][0] = str1[i];
            i++;
            data[provinceIndex][countryIndex][1] = str1[i];
        }
        for(int i=0;i<data.length;i++) {//遍历三维数组处理null值为0，便于后序排序
            for(int j=0;j<data[i].length;j++) {
                for(int k=0;k<data[i][j].length;k++)
                    if(data[i][j][k] == null) data[i][j][k] = "0";
            }
        }
        return data;
    }
    //各个市按病例数排序
    public static String[][][] sortCountry(String[][][] data){
        int provinceIndex = 0;
        provinceNumber = data.length;
        while(provinceIndex < provinceNumber) {//市排序
            for (int i = 1; i < 30; i++) {
                for (int j = 1; j < 30 - i; j++) {
                    if (Integer.parseInt(data[provinceIndex][j][1]) < Integer.parseInt(data[provinceIndex][j + 1][1])) {
                        String temp1, temp2;
                        temp1 = data[provinceIndex][j][0];
                        temp2 = data[provinceIndex][j][1];
                        data[provinceIndex][j][0] = data[provinceIndex][j + 1][0];
                        data[provinceIndex][j][1] = data[provinceIndex][j + 1][1];
                        data[provinceIndex][j + 1][0] = temp1;
                        data[provinceIndex][j + 1][1] = temp2;
                    }
                }
            }
            provinceIndex++;
        }
        return data;
    }
    //统计省的总数
    public static String[][][] allYQInAProvince(String[][][] data){
        int provinceIndex = 0;
        while(provinceIndex < provinceNumber) {//计算省病例数
            for (int i = 1; i < 30; i++) {
                int add1 = Integer.parseInt(data[provinceIndex][0][1]);
                int add2 = Integer.parseInt(data[provinceIndex][i][1]);
                add1 += add2;
                data[provinceIndex][0][1] = add1+"";
            }
            provinceIndex++;
        }
        return data;
    }
    //各个省按病例数排序
    public static String[][][] sortProvince(String[][][] data){
        provinceNumber = data.length;
        for (int i = 0;i < provinceNumber-1;i++){//省排序
            for (int j = 0;j<provinceNumber-1-i;j++)
            {
                if(Integer.parseInt(data[j][0][1])<Integer.parseInt(data[j+1][0][1])){
                    for (int p = 0; p < 30;p++){
                        String temp1, temp2;
                        temp1 = data[j][p][0];
                        temp2 = data[j][p][1];
                        data[j][p][0] = data[j+1][p][0];
                        data[j][p][1] = data[j+1][p][1];
                        data[j+1][p][0] = temp1;
                        data[j+1][p][1] = temp2;
                    }
                }
            }
        }
        return data;
    }
    //病例数相同的按拼音字母顺序排序
    public static String[][][] sortByPY(String[][][] data){
        int provinceIndex = 0;
        provinceNumber = data.length;
        while(provinceIndex < provinceNumber) {//市排序
            for (int i = 1; i < 30; i++) {
                for (int j = 1; j < 30 - i; j++) {
                    if(data[provinceIndex][j][0] != "0"){
                        if (Integer.parseInt(data[provinceIndex][j][1]) == Integer.parseInt(data[provinceIndex][j + 1][1])) {
                            if(data[provinceIndex][j][0].compareTo(data[provinceIndex][j + 1][0])>0)
                            {
                                String temp1, temp2;
                                temp1 = data[provinceIndex][j][0];
                                temp2 = data[provinceIndex][j][1];
                                data[provinceIndex][j][0] = data[provinceIndex][j + 1][0];
                                data[provinceIndex][j][1] = data[provinceIndex][j + 1][1];
                                data[provinceIndex][j + 1][0] = temp1;
                                data[provinceIndex][j + 1][1] = temp2;
                            }
                        }
                    }
                }
            }
            provinceIndex++;
        }
        return data;
    }
    //输出文件
    public static void outputFile(String[][][] data,String[] parameters,int parameterLength)throws Exception{
        FileOutputStream fop = new FileOutputStream(new File("src/"+parameters[1]));//文件输出在src文件夹下
        OutputStreamWriter writer = new OutputStreamWriter(fop, "GBK");
        if(parameterLength == 2)
        {
            for (int i = 0;i < provinceNumber;i++)
            {
                for (int j = 0;j < 30;j++){
                    if (data[i][j][0] != "0")
                    {
                        writer.append(data[i][j][0]+"\t");
                        writer.append(data[i][j][1]+"\n");
                    }
                }
                writer.append("\n");
            }
        }

        if(parameterLength == 3)
        {
            for (int i = 0;i < provinceNumber;i++)
            {
                if (data[i][0][0].equals(parameters[2])) {
                    for (int j = 0; j < 30; j++) {
                        if (data[i][j][0] != "0") {
                            writer.append(data[i][j][0] + "\t");
                            writer.append(data[i][j][1] + "\n");
                        }
                    }
                    writer.append("\n");
                }
            }
        }

        writer.close();        // 关闭写入流,同时会把缓冲区内容写入文件
        fop.close();           // 关闭输出流,释放系统资源
    }
    //main函数
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("按顺序输入参数，使用空格分隔（输入文件名 输出文件名 指定省（可选））");
        parameter = scanner.nextLine();
        scanner.close();
        parameters = parameter.split("\\s+");//分割参数存入参数数组
        parameterLength = parameters.length;
        String str = loadFile(parameters);
        String[][][] data = operateFile(str);
        data = sortCountry(data);
        data = allYQInAProvince(data);
        data = sortProvince(data);
        data = sortByPY(data);
        outputFile(data,parameters,parameterLength);
    }
}
