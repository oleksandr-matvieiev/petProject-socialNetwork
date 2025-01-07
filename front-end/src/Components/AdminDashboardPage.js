import React from "react";
import { useNavigate } from "react-router-dom";

const AdminDashboardPage = () => {
    const navigate = useNavigate();

    return (
        <div style={{ padding: "20px" }}>
            <h1 style={{ textAlign: "center", marginBottom: "30px" }}>Admin Dashboard</h1>
            <div style={{ display: "flex", flexDirection: "column", gap: "20px", alignItems: "center" }}>
                <button
                    onClick={() => navigate("/admin/users")}
                    style={{
                        padding: "15px 30px",
                        backgroundColor: "#007bff",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontSize: "18px",
                    }}
                >
                    View Users List
                </button>
                <button
                    onClick={() => navigate("/admin/send-email")}
                    style={{
                        padding: "15px 30px",
                        backgroundColor: "#28a745",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontSize: "18px",
                    }}
                >
                    Send Email to All Users
                </button>
                <button
                    onClick={() => navigate("/admin/logs")}
                    style={{
                        padding: "15px 30px",
                        backgroundColor: "#17a2b8",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontSize: "18px",
                    }}
                >
                    Check Logs
                </button>
            </div>
        </div>
    );
};

export default AdminDashboardPage;
