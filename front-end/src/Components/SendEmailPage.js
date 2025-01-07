import React, { useState } from "react";
import axios from "axios";

const SendEmailPage = () => {
    const [subject, setSubject] = useState("");
    const [message, setMessage] = useState("");
    const [status, setStatus] = useState(null);
    const token = localStorage.getItem("token");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await axios.post(
                "http://localhost:8080/api/admin/send-email-to-all",
                null,
                {
                    params: { subject, message },
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setStatus({ type: "success", message: "Emails sent successfully!" });
            setSubject("");
            setMessage("");
        } catch (error) {
            console.error("Error sending emails:", error);
            setStatus({
                type: "error",
                message: "An error occurred while sending emails. Please try again.",
            });
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h1>Send Email to All Users</h1>
            <form onSubmit={handleSubmit} style={{ maxWidth: "600px", margin: "0 auto" }}>
                <div style={{ marginBottom: "20px" }}>
                    <label htmlFor="subject" style={{ display: "block", marginBottom: "5px" }}>
                        Subject:
                    </label>
                    <input
                        type="text"
                        id="subject"
                        value={subject}
                        onChange={(e) => setSubject(e.target.value)}
                        required
                        style={{
                            width: "100%",
                            padding: "10px",
                            border: "1px solid #ccc",
                            borderRadius: "5px",
                        }}
                    />
                </div>
                <div style={{ marginBottom: "20px" }}>
                    <label htmlFor="message" style={{ display: "block", marginBottom: "5px" }}>
                        Message:
                    </label>
                    <textarea
                        id="message"
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        required
                        rows="5"
                        style={{
                            width: "100%",
                            padding: "10px",
                            border: "1px solid #ccc",
                            borderRadius: "5px",
                        }}
                    ></textarea>
                </div>
                <button
                    type="submit"
                    style={{
                        padding: "10px 20px",
                        backgroundColor: "#28a745",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                    }}
                >
                    Send Email
                </button>
            </form>
            {status && (
                <div
                    style={{
                        marginTop: "20px",
                        padding: "10px",
                        color: status.type === "success" ? "green" : "red",
                        border: `1px solid ${status.type === "success" ? "green" : "red"}`,
                        borderRadius: "5px",
                    }}
                >
                    {status.message}
                </div>
            )}
        </div>
    );
};

export default SendEmailPage;
