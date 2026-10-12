package br.com.zenon;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepositoryInterface{
    @Override
    public Optional<Transaction> findByOriginName(String name){
        String sql = "SELECT step, `type`, amount, origem_name, origin_old_balance, origin_new_balance, dest_name, dest_old_balance, dest_new_balance, is_fraud, is_flagged_fraud" +
                "       FROM zeon.`transaction`" +
                "      WHERE origem_name = ?";
        try(Connection conn = MySQLConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, name);
            try(ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Transaction transaction = mapRestultSetToTransaction(rs);
                    return Optional.of(transaction);
                }

                return Optional.empty();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }catch (SQLException ex){
            throw new RuntimeException("Erro ao procura transação: " + ex);
        }
    }

    private static @NotNull Transaction mapRestultSetToTransaction(ResultSet rs) throws SQLException {
        long step = rs.getInt("step");
        String type = rs.getString("type");
        BigDecimal amount = rs.getBigDecimal("amount");
        //
        String originName = rs.getString("origem_name");
        BigDecimal originOldBalance = rs.getBigDecimal("origin_old_balance");
        BigDecimal originNewBalance = rs.getBigDecimal("origin_new_balance");
        //
        String destName = rs.getString("dest_name");
        BigDecimal destOldBalance = rs.getBigDecimal("dest_old_balance");
        BigDecimal destNewBalance = rs.getBigDecimal("dest_new_balance");
        //
        boolean isFraud = rs.getBoolean("is_fraud");
        boolean isFlaggedFraud = rs.getBoolean("is_flagged_fraud");

        TransactionCustomer originTransactionCustomer =
                new TransactionCustomer(originName, originOldBalance, originNewBalance);
        TransactionCustomer recipientTransactionCustomer =
                new TransactionCustomer(destName, destOldBalance, destNewBalance);

        return new Transaction(step, TransactionType.valueOf(type), amount, originTransactionCustomer,
                recipientTransactionCustomer, isFraud, isFlaggedFraud);
    }

    @Override
    public void save(Transaction transaction) {
        var sql = "INSERT INTO zeon.`transaction`" +
                "(step, `type`, amount, origem_name, origin_old_balance, origin_new_balance," +
                "dest_name, dest_old_balance, dest_new_balance, is_fraud, is_flagged_fraud)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection conn = MySQLConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, transaction.step());
            ps.setString(2, transaction.type().toString());
            ps.setBigDecimal(3, transaction.amount());
            ps.setString(4, transaction.origin().name());
            ps.setBigDecimal(5, transaction.origin().oldBalance());
            ps.setBigDecimal(6, transaction.origin().newBalance());
            ps.setString(7, transaction.recipient().name());
            ps.setBigDecimal(8, transaction.recipient().oldBalance());
            ps.setBigDecimal(9, transaction.recipient().newBalance());
            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraude());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar transação: " + e);
        }
    }
}
