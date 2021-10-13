import java.io.*;
import java.util.Scanner;

public class yq {
    public static void main(String[] args) throws IOException {
        //按顺序输入参数，使用空格分隔（输入文件名 输出文件名 指定省（可选））
        String parameter = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("按顺序输入参数，使用空格分隔（输入文件名 输出文件名 指定省（可选））");
        parameter = scanner.nextLine();
        scanner.close();
        //分割参数存入参数数组
        String[] parameters = parameter.split("\\s+");
        int parameterLength = parameters.length;
        //文件输入流
        FileInputStream fip = new FileInputStream(new File("src/"+parameters[0]));//文件放在src文件夹下
        InputStreamReader reader = new InputStreamReader(fip, "GBK");
        //文件输出流
        FileOutputStream fop = new FileOutputStream(new File("src/"+parameters[1]));//文件输出在src文件夹下
        OutputStreamWriter writer = new OutputStreamWriter(fop, "GBK");
        //读取文件
        StringBuffer sb = new StringBuffer();
        while (reader.ready()) sb.append((char) reader.read());
        //存进String数组
        String str = sb.toString();
        String[] str1 = str.split("\\s+");
        int strlength = str1.length;
        //两个参数的处理方法
        if (parameterLength == 2)
        {
            String province = "";
            for (int i = 0;i < strlength;i++)
            {
                if(str1[i].equals(province)) i++;
                else
                {
                    province = str1[i];
                    if(i == 0) writer.append(str1[i]+"\n");
                    else writer.append("\n"+str1[i]+"\n");
                    i++;
                }
                if(str1[i].equals("待明确地区")) i++;
                else {
                    writer.append(str1[i] + "\t");
                    i++;
                    writer.append(str1[i] + "\n");
                }
            }
        }
        //三个参数的处理方法
        if(parameterLength == 3)
        {
            String province = parameters[2];
            boolean flag = false;
            for (int i = 0;i < strlength;i++)
            {
                if(str1[i].equals(province))
                {
                    if (!flag)
                    {
                        writer.append(str1[i]+"\n");
                        flag = true;
                        i++;
                    }
                    else i++;
                    if(str1[i].equals("待明确地区")) i++;
                    else {
                        writer.append(str1[i] + "\t");
                        i++;
                        writer.append(str1[i] + "\n");
                    }
                }
                else i = i+2;
            }
        }
        writer.close();        // 关闭写入流,同时会把缓冲区内容写入文件
        fop.close();           // 关闭输出流,释放系统资源
        reader.close();        // 关闭读取流
        fip.close();           // 关闭输入流,释放系统资源
    }
}
