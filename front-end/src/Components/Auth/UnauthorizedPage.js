import React from "react";
import { useNavigate } from "react-router-dom";

const UnauthorizedPage = () => {
    const navigate = useNavigate();

    return (
        <div style={styles.container}>
            <h1 style={styles.heading}>403 - Unauthorized</h1>
            <p style={styles.message}>
                You do not have permission to view this page.
            </p>
            <button
                style={styles.button}
                onClick={() => navigate("/")}
            >
                Back to main page
            </button>
        </div>
    );
};

const styles = {
    container: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        textAlign: "center",
        backgroundColor: "#f8f9fa",
    },
    heading: {
        fontSize: "2rem",
        color: "#dc3545",
    },
    message: {
        fontSize: "1.2rem",
        color: "#6c757d",
        margin: "10px 0",
    },
    button: {
        marginTop: "20px",
        padding: "10px 20px",
        fontSize: "1rem",
        backgroundColor: "#007bff",
        color: "#fff",
        border: "none",
        borderRadius: "5px",
        cursor: "pointer",
    },
};

export default UnauthorizedPage;
