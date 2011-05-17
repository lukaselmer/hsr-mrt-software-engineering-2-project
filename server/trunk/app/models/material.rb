# Holds information about a material which can be used in an order or for a time_entry_type
class Material < ActiveRecord::Base
  has_many :applied_materials
  has_many :orders, :through => :applied_materials
  has_many :time_entry_type_materials
  has_many :time_entry_types, :through => :time_entry_type_materials
  belongs_to :material
  has_one :material

  validates :description, :presence => true

  scope :active, where(:valid_until => nil)

  def self.for_select
    active.collect { |o| [o, o.id] }
  end
  
  def to_s
    [catalog_number, description, dimensions].compact.delete_if{|v| v.to_s.strip.blank?}.join(", ")
  end
end
