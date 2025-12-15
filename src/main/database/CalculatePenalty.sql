USE slasdb ;
GO

ALTER TRIGGER trg_CalculatePenalty
ON dbo.borrowings
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON ;

    IF UPDATE(actual_return_date)
    BEGIN
        UPDATE b
        SET b.penalty_amount = 
            CASE 
                WHEN i.actual_return_date > i.return_date THEN 
                    -- 1 TL penalty per minute
                    DATEDIFF(minute, i.return_date, i.actual_return_date) * 1.0 
                ELSE 0 
            END
        FROM dbo.borrowings b
        INNER JOIN inserted i ON b.id = i.id
        INNER JOIN deleted d ON b.id = d.id
        WHERE d.actual_return_date IS NULL 
          AND i.actual_return_date IS NOT NULL;
    END
END;
GO