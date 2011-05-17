# Holds all information about an order
class Order < ActiveRecord::Base
  has_many :applied_materials
  has_many :time_entries

  belongs_to :address
  belongs_to :customer

  validates :customer_id, :presence => true

  def self.for_select
    all.collect { |o| [o, o.id] }
  end

  def to_s
    "##{id}: #{customer}; #{created_at}"
  end
end
