USE [slasdb]; 
GO

IF NOT EXISTS (
  SELECT * FROM sys.columns 
  WHERE object_id = OBJECT_ID(N'dbo.borrowings') 
  AND name = 'penalty_amount'
)
BEGIN
    ALTER TABLE dbo.borrowings ADD penalty_amount float NULL;
    PRINT 'penalty_amount sütunu elle eklendi.';
END
GO

IF OBJECT_ID('dbo.trg_CalculatePenalty', 'TR') IS NOT NULL
   DROP TRIGGER dbo.trg_CalculatePenalty;
GO

CREATE TRIGGER trg_CalculatePenalty
ON dbo.borrowings
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(actual_return_date)
    BEGIN
        UPDATE b
        SET b.penalty_amount = 
            CASE 
                WHEN i.actual_return_date > i.return_date THEN 
                    DATEDIFF(day, i.return_date, i.actual_return_date) * 5.0 
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