class TransactionRecord
  attr_reader :date, :id, :type, :cheque_num, :payee, :memo, :amount
  
  def initialize(values)
    @date = values[0]
    @id = values[1].to_i
    @type = values[2]
    @cheque_num = values[3]
    @payee = values[4]
    @memo = values[5]
    @amount = values[6].to_f
  end
  
  def to_s
    "Date: #{date}; ID: #{id}; Type: #{type}; Cheque Num: #{cheque_num}; Payee: #{payee}; Memo: #{memo}; Amount: #{'%.2f' % amount}"
  end
end