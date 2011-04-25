class Material < ActiveRecord::Base
  has_many :applied_materials
  has_many :orders, :through => :applied_materials
  has_many :time_entry_type_materials
  has_many :time_entry_types, :through => :time_entry_type_materials
  belongs_to :material
  has_one :material
end
