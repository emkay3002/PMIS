package com.example.backup;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Witness {
    private int incidentId;
    private String name;
    private String statement;

    // Getters and setters
    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public static void saveWitnesses(int incidentId, String witnesses, DatabaseConnection database) throws SQLException {
        if (witnesses.isEmpty()) {
            return;
        }

        String[] witnessArray = witnesses.split("\n");
        String query = "INSERT INTO IncidentWitness (IncidentID, Name, Statement) VALUES (?, ?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (String witness : witnessArray) {
                String[] parts = witness.split(":", 2);
                if (parts.length == 2) {
                    Witness witnessObj = new Witness();
                    witnessObj.setIncidentId(incidentId);
                    witnessObj.setName(parts[0].trim());
                    witnessObj.setStatement(parts[1].trim());
                    pstmt.setInt(1, witnessObj.getIncidentId());
                    pstmt.setString(2, witnessObj.getName());
                    pstmt.setString(3, witnessObj.getStatement());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }
}
