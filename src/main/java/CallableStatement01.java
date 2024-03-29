import java.sql.*;

public class CallableStatement01 {

    /*
        Java'da method'lar return type sahibi olsa da olmasa da method olarak adlandırılır.
        SQL'de ise data return ediyorsa "function" denir. Return yapmiyorsa "procedure" olarak adlandırilir
    */

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/techproed",
                "postgres", "4505096sql");
        Statement st = con.createStatement();

        //CallableStatement ile function cagirmayi parametrelendirecegiz

        //1. Adım :  Functin kodunu yaz
        String sql1 = "CREATE OR REPLACE FUNCTION  toplamaF(x NUMERIC,y NUMERIC)  \n" +
                "RETURNS NUMERIC\n" +
                "LANGUAGE plpgsql\n" +
                "AS\n" +
                "$$\n" +
                "BEGIN\n" +
                "\n" +
                "RETURN x+y;\n" +
                "\n" +
                "END\n" +
                "$$";

        //2. Adım : Function'ı calistir
        st.execute(sql1);

        //3. Adım : Function'i cagir.
        CallableStatement cst1 = con.prepareCall("{? = call toplamaF(?,?)}");

        //4. Adım : Return icin registerOurParameter() methodunu,parametreler icin ise set() methodlarini uygula
        cst1.registerOutParameter(1, Types.NUMERIC);
        cst1.setInt(2, 6);
        cst1.setInt(3, 4);

        //5. Adım : execute() methodu ile CallableStatement'i calistir
        cst1.execute();

        //6. Adım : Sonucu cagirmak icin return data type'ine gore
        System.out.println(cst1.getBigDecimal(1));


        // Örnek 2 : Koninin hacmini hesaplayan bir function yazın.

        //1. Adım :  Functin kodunu yaz
        String sql2 = "CREATE OR REPLACE FUNCTION  konininHacmiF(r NUMERIC,h NUMERIC)  \n" +
                "RETURNS NUMERIC\n" +
                "LANGUAGE plpgsql\n" +
                "AS\n" +
                "$$\n" +
                "BEGIN\n" +
                "\n" +
                "RETURN 3.14*r*r*h/3;\n" +
                "\n" +
                "END\n" +
                "$$";

        //2. Adım : Function'ı calistir
        st.execute(sql2);

        //3. Adım : Function'i cagir.
        CallableStatement cst2 = con.prepareCall("{? = call konininHacmiF(?,?)}");

        //4. Adım : Return icin registerOurParameter() methodunu,parametreler icin ise set() methodlarini uygula
        cst2.registerOutParameter(1, Types.NUMERIC);
        cst2.setInt(2, 1);
        cst2.setInt(3, 6);

        //5. Adım : execute() methodu ile CallableStatement'i calistir
        cst2.execute();

        //6. Adım : Sonucu cagirmak icin return data type'ine gore
        System.out.printf("%.2f", cst2.getBigDecimal(1));

        con.close();
        st.close();
        cst1.close();
        cst2.close();
    }
}
