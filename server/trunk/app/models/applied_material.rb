class AppliedMaterial < ActiveRecord::Base
  belongs_to :material
  belongs_to :order

  validates :material, :order, :presence => true
end
