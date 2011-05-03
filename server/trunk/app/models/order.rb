class Order < ActiveRecord::Base
  has_many :applied_materials
  has_many :time_entries
  belongs_to :customer

  validates :customer_id, :presence => true
end
