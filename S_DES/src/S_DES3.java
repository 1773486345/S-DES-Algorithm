import java.util.Scanner;
public class S_DES3 {
    public static String K1;
    public static String K2;
    public static int[] P10=new int[]{3,5,2,7,4,10,1,9,8,6};
    public static int[] P8=new int[]{6, 3, 7, 4, 8, 5, 10, 9};
    public static int[] P4 = new int[] { 2, 4, 3, 1 };
    public static int[] IP_1 = new int[] { 4, 1, 3, 5, 7, 2, 8, 6 };
    public static int[] IP = new int[] { 2, 6, 3, 1, 4, 8, 5, 7 };
    public static int[] EP = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };
    public static String[][] S1_box = new String[][] {
            { "01", "00", "11", "10" }, { "11", "10", "01", "00" },
            { "00", "10", "01", "11" }, { "11", "01", "00", "10" } };
    public static String[][] S2_box = new String[][] {
            { "00", "01", "10", "11" }, { "10", "00", "01", "11" },
            { "11", "10", "01", "00" }, { "10", "01", "00", "11" } };

    /*输入主密匙，获取K1,K2*/
    public static void get_Key(String main_Key){
        main_Key=substitue(main_Key,P10); //P10
        String Ls1_L=main_Key.substring(0,5);
        String Ls1_R=main_Key.substring(5,10);
        Ls1_L=move(Ls1_L,1);//LS_1
        Ls1_R=move(Ls1_R,1);
        K1=Ls1_L+Ls1_R;
        K1=substitue(K1,P8);//K1
        String Ls2_L = move(Ls1_L, 2);//LS_2
        String Ls2_R = move(Ls1_R, 2);
        K2=Ls2_L+Ls2_R;
        K2=substitue(K2,P8);//K2
    }
    /*加密*/
    public static void encrypt(String Plainttext,String C_text,String key){
        Plainttext=substitue(Plainttext,IP);//IP置换
        String L0=Plainttext.substring(0,4);
        String R0=Plainttext.substring(4,8);
        String f1=F(R0,K1);
        String R1=xor(L0,f1);
        String L1=R0;
        String f2=F(R1,K2);
        String L2=xor(L1,f2);
        String R2=R1;
        String ciphertext=L2+R2;
        ciphertext=substitue(ciphertext,IP_1);
        if(C_text.equals(ciphertext))
        System.out.println("密钥为:"+key  );
    }
    /*解密*/
    public static void decrypt(){
        System.out.println("请输入解密信息(8bit):");
        Scanner sc=new Scanner(System.in);
        String  ciphertext=sc.nextLine(); //密文
        ciphertext=substitue(ciphertext,IP);//IP置换
        String L2=ciphertext.substring(0,4);
        String R2=ciphertext.substring(4,8);
        String R1=R2;
        String f2=F(R2,K2);
        String L1=xor(f2,L2);
        String R0=L1;
        String f1=F(L1,K1);
        String L0=xor(f1,R1);
        String Plainttext=L0+R0;
        Plainttext=substitue(Plainttext,IP_1);
        System.out.println("明文："+Plainttext);
    }
    /*置换*/
    public static  String substitue(String str,int[] P){  //输入参数为需要置换的str，以及置换顺序P
        StringBuilder new_str=new StringBuilder();
        for(int i=0;i<P.length;i++){
            new_str.append(str.charAt(P[i]-1));
        }
        return new String(new_str); //转化为String类型
    }
    /*左循环移位*/
    public static String move(String str,int n){ //输入参数为需要移位的str，移位位数n
        char[] ch=str.toCharArray();
        char[] new_ch=new char[5];
        for(int i=0;i<ch.length;i++){
            int a=((i - n) % ch.length);
            if(a<0){
                if(n==1){ //左循环1位
                    new_ch[ch.length-1]=ch[i];//new_ch[4]=ch[0]
                }
                if(n==2){//左循环2位
                    if(i==0){
                        new_ch[ch.length - 2] = ch[i];//new_ch[3]=ch[0]
                    }
                    else{
                        new_ch[ch.length - 1] = ch[i];//new_ch[4]=ch[1]
                    }
                }
            }
            else{new_ch[a] = ch[i];
            }
        }
        return new String(new_ch);
    }
    /*f函数*/
    public static String F(String str,String key){
        str=substitue(str,EP);//扩位以及置换
        str=xor(str,key);//异或
        String S1=str.substring(0,4);//S1,S2
        String S2=str.substring(4,8);
        S1 = searchSbox(S1, 1);//S盒
        S2 = searchSbox(S2, 2);
        String S = S1 + S2;
        S=substitue(S,P4);//P4置换
        return S;
    }
    /*S盒*/
    public static String searchSbox(String str, int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        sb.append(str.charAt(3));
        String ret = new String(sb);
        StringBuilder sb1 = new StringBuilder();
        sb1.append(str.charAt(1));
        sb1.append(str.charAt(2));
        String ret1 = new String(sb1);
        String retu = new String();
        if (n == 1) {
            retu = S1_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        } else {
            retu = S2_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        }
        return retu;
    }
    /*异或*/
    public static String xor(String str, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == key.charAt(i)) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }
        return new String(sb);
    }
    public static void A(){
        String binary="";
        int Max=(int)Math.pow(2,10)-1;
        for(int i=1;i<Max;i++){
            int temp=i;
            while (temp>0)
            {
                binary=(temp%2)+binary;
                temp=temp/2;
            }
            if(binary.length()<10){
                for(int k=0;binary.length()<10;k++)
                    binary="0"+binary;
            }
            System.out.println(binary+"-----");
            binary="";

        }
    }


    public static void main(String[] args) {
    /*    get_Key();
        encrypt();
        decrypt();*/
        System.out.println("请输入明文:");
        Scanner sc=new Scanner(System.in);
        String  Plainttext=sc.nextLine(); //明文
        System.out.println("请输入密文:");
        String C_text=sc.nextLine();
        String binary="";
        int Max=(int)Math.pow(2,10)-1;
        for(int i=1;i<Max;i++){
            int temp=i;
            while (temp>0)
            {
                binary=(temp%2)+binary;
                temp=temp/2;
            }
            if(binary.length()<10){
                for(int k=0;binary.length()<10;k++)
                    binary="0"+binary;
            }
            get_Key(binary);
            encrypt(Plainttext,C_text,binary);
            binary="";

        }
    }
}