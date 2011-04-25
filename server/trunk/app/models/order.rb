class Order < ActiveRecord::Base
  has_many :applied_materials
  belongs_to :customer
end
